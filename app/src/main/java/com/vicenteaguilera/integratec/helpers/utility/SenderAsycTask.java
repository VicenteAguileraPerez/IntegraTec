package com.vicenteaguilera.integratec.helpers.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SenderAsycTask extends AsyncTask<String, String, String>
{

    private String from,to;
    private ProgressDialog progressDialog;
    private Session session;
    private Context context;
    private String[] datos;


    public SenderAsycTask(Session session, String from, String to, Context context,String[] datos) {
        this.session = session;
        this.from = from;
        this.to=to;
        this.context= context;
        this.datos=datos;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Enviando", "Espere", true);
        progressDialog.setCancelable(false);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if(datos.length==2)
            {
                recuperacionCredenciales();
            }
            else
            {
                //queja o sugerencia
            }



        } catch (MessagingException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        Toast.makeText(context,
                "Se enviará un correo al email encontrado en el sistema con las credenciales", Toast.LENGTH_LONG).show();
    }


    private void recuperacionCredenciales() throws UnsupportedEncodingException, MessagingException {
        Message mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(from,"IntegraTec"));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        mimeMessage.setSubject("Recuperación de credenciales");
        String htmlText2 = "<p ALIGN=\"center\"><img  width=\"200\" height=\"200\" src=\"https://firebasestorage.googleapis.com/v0/b/integratec-itsu.appspot.com/o/integratec4.png?alt=media&token=67ee39a5-9dbb-4cf7-b0cb-5fe09d9222fc\"></p>";
        String htmlText =
                "<body> " +
                        "<h4><font size=3 face=\"Sans Serif,arial,verdana\">Hola, "+datos[0] +"</font></h4> " +
                        "<br>" +
                        htmlText2 +
                        "<hr>" +
                        "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">" + "Tus credenciales <strong>" +datos[0]/*nombre*/
                        +
                        "</strong> son:" + "</font></p>" +
                        "<p ALIGN=\"center\"><font size=3 face=\"Sans Serif,arial,verdana\">" + "<br><strong>"+to+"<br><br>"+datos[1]/*contraseña*/+"</strong>" + "</font></p>" +
                        "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Si tú no solisitaste esto no tienes porque preocuparte tus datos están protegidos.</p>" +
                        "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Saludos cordiales,</font></p>" +
                        "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#EA2925\" size=3 face=\"Sans Serif,arial,verdana\">IntegraTec</font>.</p>" +
                        "<br>" +
                        "<hr>" +
                        "<footer>" +
                        "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>" +
                        "<p ALIGN=\"justify\"><font color=\"#C5BFBF\" size=1 face=\"Sans Serif,arial,verdana\"><font color=\"#EA2925\" size=1 face=\"Sans Serif,arial,verdana\">©IntegraTec</font> from Instituto Tecnológico Superior de Uruapan, Carretera Uruapan-Carapan No. 5555 Col. La Basilia Uruapan, Michoacán. Este correo fue enviado para: <font color=\"#1a73e8\" size=1 face=\"Sans Serif,arial,verdana\">"+to+"</font> y fue enviado por <font color=\"#EA2925\" size=1 face=\"Sans Serif,arial,verdana\">IntegraTec</font></font>.</p>" +
                        "</footer>" +
                        "</body>";
        mimeMessage.setContent(htmlText, "text/html; charset=utf-8");
        Transport.send(mimeMessage);
    }
    /**
     *  try {
     *                 Message mimeMessage = new MimeMessage(session);
     *                 mimeMessage.setFrom(new InternetAddress(from,"AppVenton"));
     *                 mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
     *                 mimeMessage.setSubject("Gracias por ayudarnos a mejorar AppVenton");
     *                 String htmlText2 = "<p ALIGN=\"center\"><img  width=\"200\" height=\"200\" src=\"https://firebasestorage.googleapis.com/v0/b/appventonitecsu.appspot.com/o/icono2.png?alt=media&token=1389b4bb-2ced-4d1c-896e-ab4aa714cbca\"></p>";
     *                 String htmlText =
     *                          "<body> " +
     *                                 "<h4><font size=3 face=\"Sans Serif,arial,verdana\">Hola, </font></h4> " +
     *                                  "<br>"+
     *                                  htmlText2+
     *                                 "<hr>" +
     *                                  "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">"+"Agradecemos tu colaboración <strong>"+FirebaseConexionFirestore.getValue("Nombre")+" "+FirebaseConexionFirestore.getValue("Apellidos")+
     *                                  "</strong> evaluaremos la petición y tendremos pronta respuesta a tu petición"+"</font></p>"+
     *                                  "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Saludos cordiales,</font></p>"+
     *                                  "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#008577\" size=3 face=\"Sans Serif,arial,verdana\">AppVenton</font></p>"+
     *                                  "<br>"+
     *                                  "<hr>"+
     *
     *                                     "<footer>"+
     *                                         "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>"+
     *                                         "<p ALIGN=\"justify\"><font color=\"#C5BFBF\" size=1 face=\"Sans Serif,arial,verdana\">©AppVenton from Instituto Tecnológico Superior de Uruapan, Carretera Uruapan-Carapan No. 5555 Col. La Basilia Uruapan, Michoacán. Este correo fue enviado para: "+FirebaseConexionFirestore.getValue("Email")+" y fue enviado por AppVenton </font></p>"+
     *                                     "</footer>"+
     *                          "</body>";
     *
     *
     *
     *
     *                 mimeMessage.setContent(htmlText, "text/html; charset=utf-8");
     *
     *                 Transport.send(mimeMessage);
     *                 Message mimeMessage2 = new MimeMessage(session);
     *                 mimeMessage2.setFrom(new InternetAddress(from));
     *                 mimeMessage2.setRecipients(Message.RecipientType.TO, InternetAddress.parse(from));
     *                 mimeMessage2.setSubject(subject);
     *               //  String htmlText2 = "<p ALIGN=\"center\"><img  width=\"200\" height=\"200\" src=\"https://firebasestorage.googleapis.com/v0/b/appventonitecsu.appspot.com/o/icono2.png?alt=media&token=1389b4bb-2ced-4d1c-896e-ab4aa714cbca\"></p>";
     *                 String htmlText3 = "<body> " +
     *                                 "<hr>" +
     *                                 "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">"+"Petición de <strong>"+FirebaseConexionFirestore.getValue("Nombre")+" "+FirebaseConexionFirestore.getValue("Apellidos")+" "+FirebaseConexionFirestore.getValue("Email")+
     *                                 "</strong>, la cual menciona que:(topic datos[2]) "+"</font></p>"+
     *                                 "<p ALIGN=\"center\"><font size=3 face=\"Sans Serif,arial,verdana\">"+message+"</font></p>"+
     *                                 "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#008577\" size=3 face=\"Sans Serif,arial,verdana\">AppVenton</font></p>"+
     *                                 "<br>"+
     *                                 "<hr>"+
     *                                 "<footer>"+
     *                                 "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>"+
     *                                 "<p ALIGN=\"justify\"><font color=\"#C5BFBF\" size=1 face=\"Sans Serif,arial,verdana\">©AppVenton from Instituto Tecnológico Superior de Uruapan, Carretera Uruapan-Carapan No. 5555 Col. La Basilia Uruapan, Michoacán.</font></p>"+
     *                                 "</footer>"+
     *                                 "</body>";
     *
     *                 mimeMessage2.setContent(htmlText3, "text/html; charset=utf-8");
     *
     *                 Transport.send(mimeMessage2);
     */
}