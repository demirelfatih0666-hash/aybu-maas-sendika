package com.aybu.maassendika;

import android.app.*;import android.os.*;import android.graphics.Color;import android.view.*;import android.view.inputmethod.InputMethodManager;import android.content.*;import android.widget.*;import java.text.DecimalFormat;

public class MainActivity extends Activity{
 LinearLayout root; int green=Color.rgb(21,61,49); DecimalFormat df=new DecimalFormat("#,##0.00");
 static final double ASGARI_BRUT=33030.00, ASGARI_NET_MATRAH=28075.50, DAMGA=.00759;
 public void onCreate(Bundle b){super.onCreate(b);home();}
 TextView t(String s,int z){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(Color.DKGRAY);v.setPadding(24,14,24,14);return v;}
 Button btn(String s){Button b=new Button(this);b.setText(s);b.setTextSize(17);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(20,8,20,8);b.setLayoutParams(p);return b;}
 EditText num(String hint,String val){EditText e=new EditText(this);e.setHint(hint);e.setText(val);e.setInputType(2|8192);e.setPadding(24,12,24,12);root.addView(e);return e;}
 void base(String title){ScrollView sc=new ScrollView(this);root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(16,22,16,30);TextView h=t(title,25);h.setTextColor(green);h.setGravity(17);root.addView(h);sc.addView(root);setContentView(sc);}
 double d(EditText e){String s=e.getText().toString().trim().replace(".","").replace(',','.');return s.isEmpty()?0:Double.parseDouble(s);}
 void home(){base("AYBÜ MAAŞ & SENDİKA");root.addView(t("AYBÜ çalışanları için bordro kalemlerine göre tahmini maaş hesabı",16));Button m=btn("💰 AYBÜ Maaş Hesapla");m.setOnClickListener(v->salary());root.addView(m);Button s=btn("📢 Sendika Bilgilendirme");s.setOnClickListener(v->info("Sendika Bilgilendirme","Yetki, toplu iş sözleşmesi ve sendikal süreçlere ilişkin duyurular bu bölümde yayımlanacaktır."));root.addView(s);Button h=btn("📄 Haklarım");h.setOnClickListener(v->info("Haklarım","İzin, rapor, fazla çalışma, resmi tatil ve diğer çalışma haklarına ilişkin bilgilendirmeler."));root.addView(h);Button a=btn("⚙ Yönetim / Parametreler");a.setOnClickListener(v->admin());root.addView(a);root.addView(t("Sürüm 1.1 • Bordro hesapları bilgilendirme amaçlıdır; resmi bordro esas alınır.",13));}
 void info(String a,String b){base(a);root.addView(t(b,17));Button x=btn("← Ana Sayfa");x.setOnClickListener(v->home());root.addView(x);}
 double pref(String k,double def){return Double.longBitsToDouble(getPreferences(0).getLong(k,Double.doubleToLongBits(def)));}
 void put(String k,double x){getPreferences(0).edit().putLong(k,Double.doubleToLongBits(x)).apply();}
 void admin(){base("Yönetim / Hesap Parametreleri");root.addView(t("Bu ekrandaki değerler yalnızca bu telefondaki hesaplamayı değiştirir. Merkezi/uzaktan yönetim için sonraki adımda sunucu bağlantısı gerekir.",14));EditText y=num("Brüt günlük yevmiye",String.valueOf(pref("yev",2615.37)).replace('.',','));EditText yemek=num("Günlük yemek yardımı",String.valueOf(pref("yemek",155.50)).replace('.',','));EditText yol=num("Günlük yol yardımı",String.valueOf(pref("yol",228.60)).replace('.',','));EditText gece=num("Gece çalışma günlük/ortalama ek tutarı",String.valueOf(pref("gece",169.79)).replace('.',','));EditText prim=num("İş primi günlük/ortalama ek tutarı",String.valueOf(pref("prim",137.94)).replace('.',','));Button kay=btn("KAYDET");kay.setOnClickListener(v->{try{put("yev",d(y));put("yemek",d(yemek));put("yol",d(yol));put("gece",d(gece));put("prim",d(prim));Toast.makeText(this,"Parametreler kaydedildi",Toast.LENGTH_SHORT).show();}catch(Exception ex){Toast.makeText(this,"Rakamları kontrol edin",Toast.LENGTH_SHORT).show();}});root.addView(kay);Button x=btn("← Ana Sayfa");x.setOnClickListener(v->home());root.addView(x);}
 void salary(){base("AYBÜ Maaş Hesapla");root.addView(t("Bordroda bulunan gün ve ek ödeme kalemlerini girin. Yevmiye varsayılan olarak 2.615,37 TL'dir.",14));
  EditText y=num("Brüt günlük yevmiye",String.valueOf(pref("yev",2615.37)).replace('.',','));
  EditText normal=num("Normal çalışma günü","0"); EditText izin=num("Ücretli izin günü","0"); EditText tatil=num("Genel tatil günü","0");
  EditText fm=num("Fazla mesai / tatil çalışma brüt tutarı (TL)","0"); EditText geceGun=num("Gece çalışma günü","0"); EditText primGun=num("İş primi günü","0");
  EditText yemekGun=num("Yemek yardımı ödenen gün","0"); EditText yolGun=num("Yol yardımı ödenen gün","0"); EditText diger=num("Diğer sosyal yardımlar toplamı (TL)","0"); EditText ikramiye=num("İkramiye / tediye brüt (TL)","0"); EditText digerKes=num("Diğer kesintiler / sendika vb. (TL)","0");
  Spinner vergi=new Spinner(this);String[] vs={"Vergi dilimi %15","Vergi dilimi %20","Vergi dilimi %27","Vergi dilimi %35","Vergi dilimi %40"};vergi.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,vs));root.addView(vergi);
  TextView sonuc=t("",18);Button hesap=btn("HESAPLA");hesap.setOnClickListener(v->{try{
   double yy=d(y), gunUcret=(d(normal)+d(izin)+d(tatil))*yy, fazla=d(fm), gece=d(geceGun)*pref("gece",169.79), prim=d(primGun)*pref("prim",137.94), yemek=d(yemekGun)*pref("yemek",155.50), yol=d(yolGun)*pref("yol",228.60), sosyal=d(diger), ikr=d(ikramiye);
   double brut=gunUcret+fazla+gece+prim+yemek+yol+sosyal+ikr;
   // Bordro yaklaşımı: SGK'ya tabi brüt için kullanıcıya anlaşılır tahmin. Yemek/yol istisnalarının bordro özelindeki ayrıntıları resmi bordroda farklılaşabilir.
   double sgkMat=Math.min(brut,297270.00), sgk=sgkMat*.14, iss=sgkMat*.01;
   double gvMat=Math.max(0,brut-sgk-iss); double[] rates={.15,.20,.27,.35,.40}; double rate=rates[vergi.getSelectedItemPosition()];
   double hesapGV=gvMat*rate; double asgariIst=ASGARI_NET_MATRAH*.15; double gelir=Math.max(0,hesapGV-asgariIst);
   double damga=Math.max(0,(brut-ASGARI_BRUT)*DAMGA); double kes=d(digerKes); double net=brut-sgk-iss-gelir-damga-kes;
   sonuc.setText("BRÜT KAZANÇ: "+df.format(brut)+" TL\n\n"+"Normal/izin/tatil: "+df.format(gunUcret)+" TL\nFazla çalışma: "+df.format(fazla)+" TL\nGece çalışma: "+df.format(gece)+" TL\nİş primi: "+df.format(prim)+" TL\nYemek: "+df.format(yemek)+" TL\nYol: "+df.format(yol)+" TL\nDiğer sosyal: "+df.format(sosyal)+" TL\nİkramiye/tediye: "+df.format(ikr)+" TL\n\nKESİNTİLER\nSGK %14: -"+df.format(sgk)+" TL\nİşsizlik %1: -"+df.format(iss)+" TL\nGelir vergisi (istisna sonrası): -"+df.format(gelir)+" TL\nDamga vergisi (istisna sonrası): -"+df.format(damga)+" TL\nDiğer: -"+df.format(kes)+" TL\n\nTAHMİNİ NET: "+df.format(net)+" TL\n\nNot: Vergi dilimi kümülatif matraha göre uygulanır. Yemek/yol ve bazı sosyal yardımların SGK/vergi istisnaları bordro niteliğine göre değişebileceğinden sonuç tahminidir.");
  }catch(Exception e){sonuc.setText("Lütfen girdiğiniz rakamları kontrol edin.");}});root.addView(hesap);root.addView(sonuc);Button x=btn("← Ana Sayfa");x.setOnClickListener(v->home());root.addView(x);
 }
 @Override public void onBackPressed(){home();}
}
