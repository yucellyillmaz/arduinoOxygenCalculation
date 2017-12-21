using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace GZSProje
{
    public partial class Form2 : Form
    {
        gzsEntities gzsEntitie = new gzsEntities();

        public Form2()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string username, pass;

            if (textBox1.Text.Equals("") || textBox2.Text.Equals(""))
                MessageBox.Show("Lütfen boş alanları doldurun.");
            else
            {
                username = textBox1.Text;
                pass = textBox2.Text;

                var userList = gzsEntitie.users.Where(x => x.username == username).ToList();
                if (userList.Count == 0)
                    MessageBox.Show("Böyle bir kullanıcı bulunamadı.");
                else if (userList[0].password != pass)
                    MessageBox.Show("Kullanıcı adı veya parola hatalı.");
                else
                {
                    Form1 form = new Form1();
                    
                    getUsername.userID = userList[0].id;
                    getUsername.userName = userList[0].name;
                    form.ShowDialog();
                    this.Close();
                }
            }
        }
        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void Form2_Load(object sender, EventArgs e)
        {

        }

       
    }
}
