#include "DHT.h"
#include <SoftwareSerial.h>
#include <Adafruit_Sensor.h>

#include <LiquidCrystal.h>
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

#define DHTPIN 2     
#define DHTTYPE DHT22   
#define GASPIN A8

#define ag_ismi "YCLYLMZ"
#define ag_sifresi "yclylmz321"

DHT dht(DHTPIN, DHTTYPE);

String apiKey = "48AG08O4V5FQTDDU";     // replace with your channel's thingspeak WRITE API key

boolean thingSpeakWrite(float value1, float value2, float value3, float value4){
  String cmd = "AT+CIPSTART=\"TCP\",\"";                  // TCP connection
  cmd += "184.106.153.149";                               // api.thingspeak.com
  cmd += "\",80";
  Serial.println(cmd);
  if(Serial.find("Error")){
    Serial.println("AT+CIPSTART error");
    return false;
  }
  
  String getStr = "GET /update?api_key=";   // prepare GET string
  getStr += apiKey;
  
  getStr +="&field1="; // Oxygen
  getStr += String(value1);
  getStr +="&field2="; // Temperature
  getStr += String(value2);
  getStr +="&field3="; // Humidity
  getStr += String(value3);
  getStr +="&field4="; // Gas
  getStr += String(value4);
  getStr += "\r\n\r\n";

  // send data length
  cmd = "AT+CIPSEND=";
  cmd += String(getStr.length());
  Serial.println(cmd);
  Serial.println(getStr);
  
  if(Serial.find(">")){
    Serial.print(getStr);
  }
  else{
    Serial.println("AT+CIPCLOSE");
    return false;
  }
  return true;
}


void setup() {
  Serial.begin(115200); 

  dht.begin();
  lcd.begin(16, 2);
  lcd.setCursor(0,0);
  lcd.print("GZS - Proje");
  lcd.setCursor(0,1);
  lcd.print("Calistiriliyor..");
   
  Serial.println("AT");
  delay(5000);
  if(Serial.find("OK")){
     Serial.println("AT+CWMODE=1");
     delay(2000);
      //Serial.println("AT+CWJAP=\""+ssid+"\",\""+password+"\"");
      //String baglantiKomutu=String("AT+CWJAP=\"")+ag_ismi+"\"";
      String baglantiKomutu=String("AT+CWJAP=\"")+ag_ismi+"\",\""+ag_sifresi+"\"";
      Serial.println(baglantiKomutu);
      delay(3000);
      Serial.println("AT+CIPMUX=1");
      delay(5000);
      Serial.println("Setup completed");
  }        
}

int sayi=0;

void loop() {
  delay(2000);

  //nem ölçümü
  float h = dht.readHumidity();
  //sıcaklık ölçümü (C)
  float t = dht.readTemperature();
  //sıcaklık ölçümü (F)
  float f = dht.readTemperature(true);
  lcd.clear();
  
  //sensör değeri okudu mu? Kontrol et.
  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial.println("Isı ve nem ölçülemedi");
    
    lcd.setCursor(0,0);
    lcd.print("Veri okunamadı!");
    return;
  }


  Serial.print("Sıcaklık "+(t,1)+"\t");
  Serial.print("Nem "+(h,1)+"\t");
  
  lcd.setCursor(0,0);
  lcd.print("N:");
  lcd.setCursor(2,0);
  lcd.print(h);
  lcd.setCursor(7,0);
  lcd.print("% ");
  lcd.setCursor(9,0);
  lcd.print("S:");
  lcd.setCursor(11,0);
  lcd.print(t);  
  lcd.setCursor(15,0);
  lcd.print("C");  

  float gasValue = analogRead(GASPIN);

  Serial.print("Gaz "+gasValue+"\t");
  Serial.print(",");
  
  float sensorValue;
  float sensorVoltage;
  float Value_O2;
  sensorValue = analogRead(A0);
  sensorVoltage =(sensorValue/1024)*5.0;
  sensorVoltage = sensorVoltage/201*10000;
  Value_O2 = sensorVoltage/7.43;

  Serial.print("Oksijen"+Value_O2,1+"\t");
  
  lcd.setCursor(0,1);
  lcd.print("O2:");
  lcd.setCursor(3,1);
  lcd.print(Value_O2,1);
  lcd.setCursor(7,1);
  lcd.print("%");

  lcd.setCursor(9,1);
  lcd.print("G:");
  lcd.setCursor(11,1);
  lcd.print(gasValue);

  thingSpeakWrite(Value_O2,t,h,gasValue);                                    
   delay(7000);  
}