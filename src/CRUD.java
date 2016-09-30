import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Zach on 9/26/16.
 */
public class CRUD {
    static ArrayList<SuperHero> heroList = new ArrayList<>();
    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();

        addTestInformation();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();

                    Session session = request.session();
                    String userName = session.attribute("userName");

                    for (SuperHero s : heroList ){
                        if(s.userName.equalsIgnoreCase(userName)) {
                            s.owner = "notNull";
                        }else{
                            s.owner = null;
                        }
                    }

                    m.put("userName", userName);
                    m.put("hero", heroList);
                    return new ModelAndView(m,"home.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    String username = request.queryParams("loginName");
                    String password = request.queryParams("password");
                    if (username == null) {
                        throw new Exception("login name not found");
                    }
                    User user = users.get(username);
                    if (user == null) {
                        user = new User(username,password);
                        users.put(username, user);
                    }
                    if ( ! users.get(username).password.equals(password)){
                        response.redirect("");
                        return "";
                    }
                    Session session = request.session();
                    session.attribute("userName", username);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/newEntry",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null) {
                        throw new Exception("not logged in");
                    }

                    String secretIdentity = request.queryParams("secretIdentity");
                    String heroName = request.queryParams("heroName");
                    int age = Integer.parseInt(request.queryParams("age"));
                    String powersString = request.queryParams("powersString");
                    ArrayList<String> powersList = new ArrayList<String>(Arrays.asList( powersString.split(",")));
                    String primaryCostumeColor = request.queryParams("primaryColor");
                    String secondaryCostumeColor = request.queryParams("secondaryColor");

                    SuperHero superHero = new SuperHero(heroList.size(), userName, secretIdentity,heroName,age,powersList,primaryCostumeColor,secondaryCostumeColor);

                    heroList.add(superHero);

                    response.redirect(request.headers("Referer"));
                    return "";
                })
        );
        Spark.post(
                "/deleteHero",
                ((request, response) -> {
                    String heroName = request.queryParams("heroName");
                    SuperHero toRemove = new SuperHero();
                    for (SuperHero s: heroList) {
                        if(s.heroName.equalsIgnoreCase(heroName)){
                            toRemove = s;
                        }
                    }
                    heroList.remove(toRemove);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/editHero",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    int index = session.attribute("index");

                    String secretIdentity = request.queryParams("secretIdentity");
                    String newHeroName = request.queryParams("newHeroName");
                    int age = Integer.parseInt(request.queryParams("age"));
                    String powersString = request.queryParams("powersString");
                    ArrayList<String> powersList = new ArrayList<String>(Arrays.asList( powersString.split(",")));
                    String primaryCostumeColor = request.queryParams("primaryColor");
                    String secondaryCostumeColor = request.queryParams("secondaryColor");

                    SuperHero superHero = new SuperHero(heroList.size(), userName, secretIdentity,newHeroName,age,powersList,primaryCostumeColor,secondaryCostumeColor);

                    heroList.set(index,superHero);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.get(
                "/editPage",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    int index = Integer.parseInt(request.queryParams("index"));
                    session.attribute("index", index);

                    for (SuperHero s : heroList ){
                        if(s.userName.equalsIgnoreCase(userName)) {
                            s.owner = "notNull";
                        }else{
                            s.owner = null;
                        }
                    }
                    m.put("hero",heroList.get(index));
                    return new ModelAndView(m,"editPage.html");
                }),
                new MustacheTemplateEngine()
        );
    }

    public static void addTestInformation(){
        ArrayList<String> powers = new ArrayList<>(Arrays.asList("billionaire","martial arts","genius","world's greatest detective"));

        SuperHero superHero = new SuperHero(0,"zach","Bruce Wayne", "Batman", 30, powers,"black","grey");

        heroList.add(superHero);
    }
}
