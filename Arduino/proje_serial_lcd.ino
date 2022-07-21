#include "DHT.h"

#include <LiquidCrystal.h>
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

#define DHTPIN 2     
#define DHTTYPE DHT22   
#define GASPIN A8

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600); 
  Serial.println("bağlantı yok, bağlantı yok, bağlantı yok, bağlantı yok");
  
  dht.begin();
  lcd.begin(16, 2);
  lcd.setCursor(0,0);
  lcd.print("proje");
  lcd.setCursor(0,1);
  lcd.print("Calistiriliyor..");
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
  Serial.println("bağlantı yok, bağlantı yok, bağlantı yok, bağlantı yok");
    
    lcd.setCursor(0,0);
    lcd.print("Veri okunamadı!");
    return;
  }


  Serial.print(t,1);
  Serial.print(","); 
  Serial.print(h,1);
  Serial.print(","); 
  
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

  Serial.print(gasValue);
  Serial.print(",");
  
  float sensorValue;
  float sensorVoltage;
  float Value_O2;
  sensorValue = analogRead(A0);
  sensorVoltage =(sensorValue/1024)*5.0;
  sensorVoltage = sensorVoltage/201*10000;
  Value_O2 = sensorVoltage/7.43;

  Serial.print(Value_O2,1);
  
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
}