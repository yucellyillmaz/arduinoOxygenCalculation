using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO.Ports;

namespace proje
{
    public partial class Form1 : Form
    {
        string[] ports = SerialPort.GetPortNames();
        gzsEntities gzsEntitie = new gzsEntities();

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            label19.Text = getUsername.userName;
            label24.Visible = false;
            label28.Visible = false;
            label20.Visible = false;
            label23.Visible = false;
            label25.Visible = false;
            label26.Visible = false;

            foreach (string port in ports)
            {
                comboBox1.Items.Add(port); // Port isimlerini combobox1'de gösteriyoruz.
                comboBox1.SelectedIndex = 0;
            }
            comboBox2.Items.Add("2400");  // Baudrate'leri kendimiz combobox2'ye giriyoruz.
            comboBox2.Items.Add("4800");
            comboBox2.Items.Add("9600");
            comboBox2.Items.Add("19200");
            comboBox2.Items.Add("115200");
            comboBox2.SelectedIndex = 2;

            label5.Text = "Bağlantı Kapalı";
            label5.ForeColor = Color.Red;

            sonucYaz("bağlantı yok,bağlantı yok,bağlantı yok,bağlantı yok");

            kullaniciDoldur();
            gecmisHesapDoldur();
        }

        public void kullaniciDoldur()
        {
            var userList = gzsEntitie.users.ToList();
            dataGridView1.DataSource = userList;
        }

        public void gecmisHesapDoldur()
        {
            //var calculationList = gzsEntitie.calculations.ToList();
            //dataGridView2.DataSource = calculationList;

            var calculationList = (from c in gzsEntitie.calculations
                              join l in gzsEntitie.logs on c.log_id equals l.id
                              join u in gzsEntitie.users on c.user_id equals u.id
                              select new
                              {
                                  kullanici_adi = u.username,
                                  en = c.width,
                                  boy = c.length,
                                  yukseklik = c.height,
                                  yasam_suresi = c.remaining_time,
                                  tarih = c.date,
                                  nem = l.humidity,
                                  sicaklik = l.temperature,
                                  gaz = l.gas,
                                  oksijen = l.oxygen
                              }).OrderByDescending(x=> x.tarih).ToList();
            dataGridView2.DataSource = calculationList;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            timer1.Start();

            if (serialPort1.IsOpen == false)
            {
                if (comboBox1.Text == "")
                    return;
                serialPort1.PortName = comboBox1.Text;  // combobox1'e zaten port isimlerini aktarmıştık.
                serialPort1.BaudRate = Convert.ToInt32(comboBox2.Text); //Seri Haberleşme baudrate'i combobox2 'de seçilene göre belirliyoruz.
                try
                {
                    serialPort1.Open(); //Haberleşme için port açılıyor
                    label5.ForeColor = Color.Green;
                    label5.Text = "Bağlantı Kuruldu";
                    label3.ImageIndex = 1;
                }
                catch (Exception hata)
                {
                    MessageBox.Show("Hata:" + hata.Message);
                }
            }
            else
            {
                label5.Text = "Bağlantı Zaten Kurulmuş.";
            }
       
        }

        public void sonucYaz(string sonuc)
        {
            string[] sonucList = sonuc.Split(',');
            for (int i = 0; i < sonucList.Length; i++)
            {
                if (i == 0)
                {
                    lblS.Text = sonucList[0];
                    lblN.Text = sonucList[1];
                    lblG.Text = sonucList[2];
                    lblO.Text = sonucList[3];

                    //save to db
                    if (!sonucList[0].Equals("bağlantı yok"))
                    {
                        log log = new log();
                        log.temperature = Convert.ToDouble(sonucList[0]);
                        log.humidity = Convert.ToDouble(sonucList[1]);
                        log.gas = Convert.ToDouble(sonucList[2]);
                        log.oxygen = Convert.ToDouble(sonucList[3]);
                        log.user_id = getUsername.userID;
                        log.date = DateTime.Now;
                        gzsEntitie.logs.Add(log);
                        gzsEntitie.SaveChanges();

                        label20.Visible = true;
                        label23.Visible = true;
                        label25.Visible = true;
                        label26.Visible = true;

                    }
                }
                else
                    break;
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            timer1.Stop();
            if (serialPort1.IsOpen == true)
            {
                serialPort1.Close();
                label5.ForeColor = Color.Red;
                label5.Text = "Bağlantı Kapalı";
                label3.ImageIndex = 0;
            }
            else
            {
                label5.Text = "Bağlantı Zaten Kapalı";
            }
        }

        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            if (serialPort1.IsOpen == true)
            {
                serialPort1.Close();
            }
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            try
            {
                string sonuc = serialPort1.ReadExisting();//Serial.print kodu ile gelen analog veriyi alıyoruz,string formatında sonuc'a atıyoruz
                sonucYaz(sonuc);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                timer1.Stop();
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            string name, username, pass;

            if (textBox1.Text.Equals("") || textBox2.Text.Equals("") || textBox3.Text.Equals(""))
                MessageBox.Show("Lütfen boş alan bırakmayın");
            else
            {
                name = textBox1.Text;
                username = textBox2.Text;
                pass = textBox3.Text;

                user user = new user();
                user.name = name;
                user.username = username;
                user.password = pass;

                gzsEntitie.users.Add(user);
                gzsEntitie.SaveChanges();

                textBox1.Text = "";
                textBox2.Text = "";
                textBox3.Text = "";
            }
            kullaniciDoldur();
        }

        private void tabPage1_Click(object sender, EventArgs e)
        {

        }

        private void button4_Click(object sender, EventArgs e)
        {
            if (textBox4.Text.Equals("") || textBox5.Text.Equals("") || textBox6.Text.Equals(""))
                MessageBox.Show("Lütfen boş alanları doldurun");
            else if (lblO.Text == "bağlantı yok" ||serialPort1.IsOpen == false)
                MessageBox.Show("Bağlantı yok");
            else
            {
                double a, b, h, v, o2, t;
                a = Convert.ToDouble(textBox4.Text);
                b = Convert.ToDouble(textBox5.Text);
                h = Convert.ToDouble(textBox6.Text);
                v = a * b * h;
                o2 = v * (Convert.ToDouble(lblO.Text) / 100);
                o2 = o2 * 1000;
                t = o2 / 1.39;

                int min,hour,day;
                string time = "BU ORTAMDA ";
                min = Convert.ToInt32(t) % 60 ;
                hour = Convert.ToInt32(t - min) / 60;
                day = hour / 24;
                hour = hour - (day*24);
                if (day != 0)
                    time += day.ToString() + " GÜN ";
                if (hour != 0)
                    time += hour.ToString() + " SAAT ";
                if (min != 0)
                    time += min.ToString() + " DAKİKA ";
                time += "HAYATTA KALABİLİRSİNİZ!";
                label24.Text = time;
                label24.Visible = true;
                label28.Visible = true;
                textBox4.Text = "";
                textBox5.Text = "";
                textBox6.Text = "";
                
                //save new calculation
                calculation calculation = new calculation();
                calculation.width = a;
                calculation.length = b;
                calculation.height = h;
                calculation.remaining_time = Convert.ToInt32(t);
                var lastLog = gzsEntitie.logs.OrderByDescending(x => x.id).Take(1).ToList();
                calculation.log_id = lastLog[0].id;
                calculation.user_id = getUsername.userID;
                calculation.date = DateTime.Now;
                gzsEntitie.calculations.Add(calculation);
                gzsEntitie.SaveChanges();


                gecmisHesapDoldur();
                MessageBox.Show("Hesaplama sonuçları veritabanına başarıyla kaydedildi.");
            }
        }

        private void label26_Click(object sender, EventArgs e)
        {

        }

    }
}
