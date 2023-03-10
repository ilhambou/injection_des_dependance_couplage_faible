import Metier.IB_CreditMetier;
import Metier.IB_IMetier;
import Modele.IB_Credit;
//import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;
import dao.IB_CreditDao;
import dao.IB_IDao;
import presentation.IB_CreditControleur;
import presentation.IB_IControleur;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Scanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IB_Simulateur {


        static Scanner clavier = new Scanner(System.in);
        static IB_IControleur creditControleur;//test2






        public static void test2() throws Exception
        {
            String daoClass;
            String serviceClass;
            String controllerClass;

            Properties properties = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream propertiesFile = classLoader.getResourceAsStream("config.properties");
            if(propertiesFile == null) throw new Exception("fichier config introuvable");
            else {
                try {
                    properties.load(propertiesFile);
                    daoClass = properties.getProperty("DAO");
                    serviceClass = properties.getProperty("SERVICE");
                    controllerClass = properties.getProperty("CONTROLLER");
                    propertiesFile.close();
                }catch (IOException e)
                {
                    throw new Exception("probleme au niveau du fichier");

                }finally {
                    properties.clear();
                }
                try {
                    Class cDao = Class.forName(daoClass);
                    Class cMetier = Class.forName(serviceClass);
                    Class cControleur = Class.forName(controllerClass);
                    var dao = (IB_IDao<IB_Credit, Long>)cDao.getDeclaredConstructor().newInstance();
                    var metier = (IB_IMetier) cMetier.getDeclaredConstructor().newInstance();
                    var creditControleur = (IB_IControleur) cControleur.getDeclaredConstructor().newInstance();

                    ///
                    /*Method setDao = cMetier.getMethod("setCreditDao",IB_IDao.class);
                    setDao.invoke(metier,dao);
                    ///
                    Method setMetier = cControleur.getMethod("setCreditMetier", IB_CreditMetier.class);
                    setMetier.invoke(creditControleur,metier);*/

                    Method setDao = cMetier.getMethod("setCreditDao", IB_IDao.class);
                    setDao.invoke(metier, dao);

                    Method setMetier = cControleur.getMethod("setCreditMetier", IB_IMetier.class);
                    setMetier.invoke(creditControleur, metier);

                    creditControleur.afficher_Mensualite(1L);



                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }



        private static boolean estUnNombre(String input)
        {
            try
            {
                Integer.parseInt(input);
                return true;
            }catch (Exception e)
            {
                return false;
            }
        }

        public static void test1()
        {
            var dao = new IB_CreditDao();
            var metier = new IB_CreditMetier();
            var controleur = new IB_CreditControleur();

            metier.setCreditDao(dao);
            controleur.setCreditMetier(metier);
            String rep = "";
            do{
                System.out.println("=>[Test 1] Calcule de mensualite ");
                try {
                    String input = "";
                    while (true)
                    {
                        System.out.print("entrer l 'id : ");
                        input = clavier.nextLine();
                        if(estUnNombre(input))
                        {
                            break;
                        }
                        else
                        {
                            System.out.println("entrerr non valide");
                        }
                    }
                    Long id = Long.parseLong(input);
                    controleur.afficher_Mensualite(id);
                }catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
                System.out.println("voulez vous quitter : ");
                rep = clavier.nextLine();
            }while (!rep.equalsIgnoreCase("oui"));
            System.out.println("bye");


        }

    public static void test3() throws Exception
    {
        ApplicationContext context =new ClassPathXmlApplicationContext("spring-ioc.xml");
        creditControleur = (IB_IControleur) context.getBean("controleur");
        creditControleur.afficher_Mensualite(1L);
    }

    public static void test4() throws Exception
    {
        ApplicationContext context = new AnnotationConfigApplicationContext("dao","metier","presentation");
        creditControleur = (IB_IControleur) context.getBean(IB_IControleur.class);
        creditControleur.afficher_Mensualite(1L);



    }

    public static void main(String[] args) throws Exception{
           test4();

    }

    }
