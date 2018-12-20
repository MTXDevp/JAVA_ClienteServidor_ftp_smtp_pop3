import com.sun.java.browser.plugin2.DOM;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEvent;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEventListener;
import com.teamdev.jxbrowser.chromium.dom.events.DOMEventType;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

//https://jxbrowser.support.teamdev.com/support/solutions/
//pusheo
public class ControladorLogin {

    //xd

    static Conexion conexion;
    VistaLogin vloging;
    DOMDocument document;
    DOMElement botonIniciarSesion;
    DOMElement cajaIniciarSesion;
    DOMElement botonRegistrarse;
    static Browser browser;
    BrowserView view;
    DOMElement cajaRegistrarse;
    String usuario;
    String contraseña;

    ControladorLogin(){

        conexion = new Conexion();

        LoggerProvider.setLevel(Level.OFF);

        browser = new Browser();
        view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        String ruta = "C:\\Users\\USUARIO\\Desktop\\ClienteServidor-ftp-smtp-pop3\\IntelliJ_version\\PROYECTO_ENRIQUE_INTELLIJ\\src\\html\\login.html";
        browser.loadURL(ruta);

        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {

                if (event.isMainFrame()) {

                String url = event.getValidatedURL();
                System.out.println("la url es : " + url);

                if(url.equals("file:///C:/Users/USUARIO/Desktop/ClienteServidor-ftp-smtp-pop3/IntelliJ_version/PROYECTO_ENRIQUE_INTELLIJ/src/html/Registrarse.html")){

                    System.out.println("Estas en la ventana registrarse");
                                Browser browser = event.getBrowser();
                                JSValue value = browser.executeJavaScriptAndReturnValue("window");
                                value.asObject().setProperty("Account", new getDatosRegistro());

                }else if(url.equals("file:///C:/Users/USUARIO/Desktop/ClienteServidor-ftp-smtp-pop3/IntelliJ_version/PROYECTO_ENRIQUE_INTELLIJ/src/html/pruebaMenu.html")) {

                    System.out.println("Estas en la ventana pruebaMenu");
                    DOMDocument document = browser.getDocument();
                    DOMElement linkCerrar = document.findElement(By.id("salir"));
                    System.out.println("el link es : " + linkCerrar);

                    linkCerrar.addEventListener(DOMEventType.OnClick,
                            (DOMEvent dome) -> {
                                System.exit(0);
                            }, false);


                }else if(url.equals("file:///C:/Users/USUARIO/Desktop/ClienteServidor-ftp-smtp-pop3/IntelliJ_version/PROYECTO_ENRIQUE_INTELLIJ/src/html/server-FTP.html")){

                    System.out.println("Estas en la ventana server-FTP");
                    //ControladorFTP cftp = new ControladorFTP(browser, event);

                    Browser browser = event.getBrowser();
                    JSValue value = browser.executeJavaScriptAndReturnValue("window");
                    value.asObject().setProperty("Account", new getDireccionFTP());

                }else{

                    Browser browser = event.getBrowser();
                    JSValue value = browser.executeJavaScriptAndReturnValue("window");
                    value.asObject().setProperty("Account", new getUsuarioContraseña());

                     }// final si es el main frame
                }
            }
        });
    }


    //AÑADIR RUTAS
    public void Eventos(Conexion c){

        botonIniciarSesion.addEventListener(DOMEventType.OnClick,
                        (DOMEvent dome) -> {
                        }, false);

        botonRegistrarse.addEventListener(DOMEventType.OnClick,
                (DOMEvent dome)-> {
                  String ruta= "https://www.google.com/";
                  vloging.getBrowser().loadURL(ruta);
                }, false);
    }
    public static class getUsuarioContraseña {
        public void save(String usuario, String contraseña) {
            System.out.println("Usuario    = " + usuario);
            System.out.println("Contraseña = " + contraseña);

            conexion.CheckLogin(usuario, contraseña);

            try {
                if(conexion.getRs().next()){

                    browser.loadURL("C:\\Users\\USUARIO\\Desktop\\ClienteServidor-ftp-smtp-pop3\\IntelliJ_version\\PROYECTO_ENRIQUE_INTELLIJ\\src\\html\\pruebaMenu.html");
                    //ControladorMenu cm = new ControladorMenu(browser);
                    }else{
                    JOptionPane.showMessageDialog(null, "CREDENCIALES ERRONEAS", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                System.out.println("SE HA PRODUCIDO UN ERROR CONSULTANDO LA BASE DE DATOS");
                System.out.println("DETALLES : ");
                System.out.println(e.getMessage());
            }
        }
    }
    public static class getDatosRegistro{

        public getDatosRegistro(){
            System.out.println("entro en get datos registro");
        }
        public void save(String correo, String usuario, String contraseña, String contraseña2) {
            System.out.println("Correo Electrónico    = " + correo);
            System.out.println("Usuario = " + usuario);
            System.out.println("Contraseña = " + contraseña);
            System.out.println("Contraseña2 = " + contraseña2);

            if(!contraseña.equals(contraseña2)){

                JOptionPane.showMessageDialog(null, "LAS CONTRASEÑAS NO COINCIDEN", "ERROR", JOptionPane.WARNING_MESSAGE);

            }else{
                conexion.InsertNewUsuario(usuario, contraseña, correo);
            }
        }
    }
    public static class getDireccionFTP {
        public void save(String direccionFTP) {

            System.out.println("FTP : " + direccionFTP);
            ControladorFTP cFTP = new ControladorFTP(direccionFTP);

        }
    }
}

