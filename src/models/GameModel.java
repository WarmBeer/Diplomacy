package models;

import application.Main;
import domains.Army;
import domains.Fleet;
import domains.Province;
import domains.Unit;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

import static application.Main.GAME_VIEW;

public class GameModel implements Model {

    private enum unitType {
        Army,
        Fleet
    }

    Group root; //Kaart en UI render groep
    Group troops; //Troepen render groep
    Group points; //Provincie punt render groep
    ArrayList<Province> provinces = new ArrayList<Province>();

    @FXML
    public void show(Stage stage) throws Exception{

        root = new Group();
        troops = new Group();
        points = new Group();

        Parent pane = FXMLLoader.load(
                getClass().getResource(GAME_VIEW));

        Scene scene = new Scene( root, 1920, 1080 );
        root.getChildren().addAll(pane, troops, points);

        stage.setScene(scene);
    }

    @FXML
    public void init() {
        initProvinces();

        //Maak troepen aan
        for(Province province: provinces){


            switch(province.getProvinceType()){
                case SEA:
                    province.setImage(new Image("Point coastal.png"));
                    //createUnit(unitType.Fleet, province, province.getX(), province.getY());
                    break;
                case LAND:
                    province.setImage(new Image("Point.png"));
                    //createUnit(unitType.Army, province, province.getX(), province.getY());
                    break;
            }

            province.setTranslateX(-25);
            province.setTranslateY(-35);

            province.setScaleX(0.2);
            province.setScaleY(0.2);

            province.setX(province.getX());
            province.setY(province.getY());

            province.setOpacity(0.6);

            province.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    Province provincePoint = (Province ) event.getTarget();

                    for(Node node : points.getChildren()) {
                        Province nodeProvince = (Province) node;
                        nodeProvince.setOpacity(0.6);
                        nodeProvince.setScaleX(0.2);
                        nodeProvince.setScaleY(0.2);
                    }

                    provincePoint.setOpacity(1);
                    provincePoint.setScaleX(0.3);
                    provincePoint.setScaleY(0.3);
                }
            });

            points.getChildren().add(province);

        }

    }


    public void createUnit(unitType unit, Province province, double x, double y) {
        Unit troop = null;

        System.out.println(x);

        switch (unit) {
            case Army:
                troop = new Army(province);
                break;
            case Fleet:
                troop = new Fleet(province);
                break;
        }

        province.addUnit(troop);

        moveUnit(troop, x, y);

        //Render troepen
        troops.getChildren().add(troop);

        troop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

            }
        });

    }

    public void initProvinces() {

        //----GERMANY//----
        Province kie = new Province("Kiel", "kie", true, 580, 540);
        provinces.add(kie);
        Province ber = new Province("Berlin", "ber", true, 675, 535);
        provinces.add(ber);
        Province pru = new Province("Prussia", "pru", false, 738, 505);
        provinces.add(pru);
        Province ruh = new Province("Ruhr", "ruh", false, 540, 601);
        provinces.add(ruh);
        Province mun = new Province("Munich", "mun", true, 603, 604);
        provinces.add(mun);
        Province sil = new Province("silesia", "sil", false, 715, 550);
        provinces.add(sil);

        kie.addBorder(ber);
        kie.addBorder(ruh);
        kie.addBorder(mun);
        ber.addBorder(pru);
        ber.addBorder(sil);
        ber.addBorder(mun);
        ruh.addBorder(mun);
        mun.addBorder(sil);
        sil.addBorder(pru);

        //----FRANCE//----

        Province pic = new Province("Picardy", "pic", false, 434, 565);
        provinces.add(pic);
        Province bre = new Province("Brest", "bre", true, 335, 627);
        provinces.add(bre);
        Province par = new Province("Paris", "par", true, 403, 649);
        provinces.add(par);
        Province bur = new Province("Burgundy", "bur", false, 483, 624);
        provinces.add(bur);
        Province gas = new Province("Gascony", "gas", false, 346, 722);
        provinces.add(gas);
        Province mar = new Province("Marseilles", "mar", true, 473, 735);
        provinces.add(mar);


        pic.addBorder(bre);
        pic.addBorder(par);
        pic.addBorder(bur);
        bre.addBorder(par);
        bre.addBorder(gas);
        par.addBorder(gas);
        par.addBorder(bur);
        bur.addBorder(gas);
        bur.addBorder(mar);
        gas.addBorder(mar);


        //----ITALY//----
        Province pie = new Province("piedmont", "pie", false, 550, 721);
        provinces.add(pie);
        Province ven = new Province("Venice", "ven", true, 624, 734);
        provinces.add(ven);
        Province tus = new Province("Tuscany", "tus", false, 607, 776);
        provinces.add(tus);
        Province rom = new Province("Rome", "rom", true, 650, 811);
        provinces.add(rom);
        Province apu = new Province("Apulia", "apu", false, 748, 829);
        provinces.add(apu);
        Province nap = new Province("Naples", "nap", true, 731, 864);
        provinces.add(nap);


        pie.addBorder(ven);
        pie.addBorder(tus);
        ven.addBorder(tus);
        ven.addBorder(apu);
        ven.addBorder(rom);
        tus.addBorder(rom);
        rom.addBorder(apu);
        rom.addBorder(nap);
        nap.addBorder(apu);



        //----AUSTRIA (hungary?)//-----
        Province boh = new Province("Bohemia", "boh", false, 698, 625);
        provinces.add(boh);
        Province gal = new Province("Galicia", "gal", false, 907, 618);
        provinces.add(gal);
        Province tyr = new Province("Tyrolia", "tyr", false, 667, 650);
        provinces.add(tyr);
        Province vie = new Province("Vienna", "vie", true, 748, 689);
        provinces.add(vie);
        Province bud = new Province("Budapest", "bud", true, 846, 691);
        provinces.add(bud);
        Province tri = new Province("Trieste", "tri", true, 707, 715);
        provinces.add(tri);


        boh.addBorder(gal);
        boh.addBorder(vie);
        boh.addBorder(tyr);
        gal.addBorder(bud);
        gal.addBorder(vie);
        tyr.addBorder(vie);
        tyr.addBorder(tri);
        vie.addBorder(bud);
        vie.addBorder(tri);
        bud.addBorder(tri);


        //----AUSTRIA-HUNGARY//---//---
        Province rum = new Province("Romania", "rum", true, 1083, 1083);
        provinces.add(rum);
        Province ser = new Province("Serbia", "ser", true, 870, 805);
        provinces.add(ser);
        Province alb = new Province("Albania", "alb", false, 838, 840);
        provinces.add(alb);
        Province bul = new Province("Bulgaria", "bul", true, 941, 799);
        provinces.add(bul);
        Province gre = new Province("Greece", "gre", false, 879, 845);
        provinces.add(gre);


        rum.addBorder(ser);
        rum.addBorder(bul);
        ser.addBorder(bul);
        ser.addBorder(alb);
        ser.addBorder(gre);
        alb.addBorder(gre);
        bul.addBorder(gre);


        //-----TURKEY//---//---
        Province con = new Province("Constantinople", "con", true, 1030, 844);
        provinces.add(con);
        Province smy = new Province("Smyrna", "smy", true, 1071, 880);
        provinces.add(smy);
        Province ank = new Province("Ankara", "ank", true, 1175, 818);
        provinces.add(ank);
        Province arm = new Province("Armenia", "arm", false, 1339, 827);
        provinces.add(arm);
        Province syr = new Province("Syria", "syr", false, 1342, 945);
        provinces.add(syr);


        con.addBorder(ank);
        con.addBorder(smy);
        smy.addBorder(ank);
        smy.addBorder(arm);
        smy.addBorder(syr);
        ank.addBorder(arm);
        arm.addBorder(syr);


        //-----ENGLANG//---//---//---//---
        Province cly = new Province("Clyde", "cly", false, 339, 400);
        provinces.add(cly);
        Province edi = new Province("Edinburgh", "edi", true, 372, 409);
        provinces.add(edi);
        Province lvp = new Province("Liverpool", "lvp", true, 359, 474);
        provinces.add(lvp);
        Province yor = new Province("York", "yor", false, 389, 501);
        provinces.add(yor);
        Province wal = new Province("Wales", "wal", false, 319, 525);
        provinces.add(wal);
        Province lon = new Province("London", "lon", true, 393, 537);
        provinces.add(lon);

        cly.addBorder(edi);
        cly.addBorder(lvp);
        edi.addBorder(lvp);
        edi.addBorder(yor);
        lvp.addBorder(yor);
        lvp.addBorder(wal);
        yor.addBorder(lon);
        yor.addBorder(wal);
        wal.addBorder(lon);


        //------RUSLAND------

        Province war = new Province("Warsaw", "war", true, 857, 552);
        provinces.add(war);
        Province lvn = new Province("Livonia", "lvn", false, 937, 430);
        provinces.add(lvn);
        Province urk = new Province("Urkaine", "urk", false, 1060, 590);
        provinces.add(urk);
        Province mos = new Province("Moscow", "mos", true, 1244, 412);
        Province stp = new Province("St. Petersburg", "stp", true, 1048, 344);
        provinces.add(mos);
        provinces.add(stp);
        Province fin = new Province("Finland", "fin", false, 883, 290);
        provinces.add(fin);
        Province sev = new Province("Sevastopol", "sev", true, 1384, 637);
        provinces.add(sev);


        war.addBorder(lvn);
        war.addBorder(mos);
        war.addBorder(urk);
        lvn.addBorder(stp);
        lvn.addBorder(mos);
        urk.addBorder(mos);
        urk.addBorder(sev);
        mos.addBorder(stp);
        mos.addBorder(sev);
        stp.addBorder(fin);


        //-----------spain & africa-------

        Province spa = new Province("Spain", "spa", true, 204, 848);
        provinces.add(spa);
        Province por = new Province("Portugal", "por", true, 92, 827);
        provinces.add(por);
        Province naf = new Province("N. Africa", "naf", false,200, 965);
        provinces.add(naf);
        Province tun = new Province("Tunis", "tun", true, 563, 944);
        provinces.add(tun);


        spa.addBorder(por);
        naf.addBorder(tun);


        //-----NORWAY, SWEDEN DENMARK------

        Province nwy = new Province("Norway", "nwy", true, 615, 338);
        provinces.add(nwy);
        Province swe = new Province("Sweden", "swe", true, 710, 373);
        provinces.add(swe);
        Province den = new Province("Denmark", "den", true, 628, 418);
        provinces.add(den);


        nwy.addBorder(swe);


        //----independent provinces------

        Province hol = new Province("Holland", "hol", true, 507, 516);
        provinces.add(hol);
        Province bel = new Province("Belgium", "bel", true, 491, 553);
        provinces.add(bel);

        bel.addBorder(hol);

        //BORDERS GERMANY -> france and austria (hol & beg);

        kie.addBorder(hol);
        ruh.addBorder(bel);
        ruh.addBorder(bur);
        mun.addBorder(tyr);
        mun.addBorder(boh);
        sil.addBorder(boh);
        sil.addBorder(gal);

        //BORDERS FRANCE -> italy (hol & beg);
        mar.addBorder(pie);
        pic.addBorder(bel);
        bur.addBorder(bel);

        //BORDERS ITALY -> austria
        pie.addBorder(tyr);
        ven.addBorder(tyr);
        ven.addBorder(tri);

        //BORDERS AUSTRIA -> bulgaria?
        gal.addBorder(rum);
        bud.addBorder(rum);
        bud.addBorder(ser);
        tri.addBorder(ser);
        tri.addBorder(alb);

        //BORDERS BULGARIA? -> turkey
        bul.addBorder(con);

        //BORDERS FRANCE -> spain
        gas.addBorder(spa);
        gas.addBorder(spa);

        //BORDERS RUSSIA TURKEY, BULGARIA, AUSTRALIA FRANCE, SWEDEN
        sev.addBorder(arm);
        sev.addBorder(rum);
        urk.addBorder(rum);
        urk.addBorder(gal);
        war.addBorder(gal);
        war.addBorder(sil);
        war.addBorder(pru);
        fin.addBorder(swe);
        fin.addBorder(nwy);

        //OVERIGE
        den.addBorder(kie);

        // -----SEA'S------
        Province bar = new Province("Barents Sea", "bar", false, 1004, 50, Province.ProvinceType.SEA);
        provinces.add(bar);
        Province nrg = new Province("NRG Norwegian Sea", "nrg", false, 524, 186, Province.ProvinceType.SEA);
        provinces.add(nrg);
        Province nth = new Province("NTH North Sea", "nth", false, 481, 446, Province.ProvinceType.SEA);
        provinces.add(nth);
        Province ska = new Province("SKA", "ska", false, 650, 400, Province.ProvinceType.SEA);
        provinces.add(ska);
        Province bal = new Province("Baltic Sea", "bal", false, 798, 450, Province.ProvinceType.SEA);
        provinces.add(bal);
        Province bot = new Province("BOT Gulf of Botnia", "bot", false, 803, 312, Province.ProvinceType.SEA);
        provinces.add(bot);
        Province hel = new Province("HEL Helgoland Bight", "hel", false, 539, 484, Province.ProvinceType.SEA);
        provinces.add(hel);
        Province eng = new Province("English channel", "eng", false, 350, 555, Province.ProvinceType.SEA);
        provinces.add(eng);
        Province iri = new Province("Irish Sea", "iri", false, 260, 520, Province.ProvinceType.SEA);
        provinces.add(iri);
        Province nat = new Province("N. Atlantic", "nat", false, 135, 343, Province.ProvinceType.SEA);
        provinces.add(nat);
        Province mid = new Province("Mid Atlantic", "mid", false, 99, 655, Province.ProvinceType.SEA);
        provinces.add(mid);
        Province wes = new Province("West Mediterranean", "wes", false, 414, 870, Province.ProvinceType.SEA);
        provinces.add(wes);
        Province gol = new Province("GOL Golf of Legon", "gol", false, 466, 833, Province.ProvinceType.SEA);
        provinces.add(gol);
        Province tyn = new Province("Tyrhennian Sea", "tyn", false, 611, 895, Province.ProvinceType.SEA);
        provinces.add(tyn);
        Province ion = new Province("Ionian Sea", "ion", false, 789, 937, Province.ProvinceType.SEA);
        provinces.add(ion);
        Province adr = new Province("Adriantic Sea", "adr", false, 760, 804, Province.ProvinceType.SEA);
        provinces.add(adr);
        Province aeg = new Province("Aegean Sea", "aeg", false, 973, 933, Province.ProvinceType.SEA);
        provinces.add(aeg);
        Province eas = new Province("East Mid", "eas", false, 1148, 950, Province.ProvinceType.SEA);
        provinces.add(eas);
        Province bla = new Province("Black Sea", "bla", false, 1127, 763, Province.ProvinceType.SEA);
        provinces.add(bla);


        //ZEE
        bar.addBorder(stp);
        bar.addBorder(swe);
        bar.addBorder(nrg);
        nrg.addBorder(nwy);
        nrg.addBorder(nth);
        nrg.addBorder(edi);
        nrg.addBorder(cly);
        nrg.addBorder(nat);
        nth.addBorder(nwy);
        nth.addBorder(edi);
        nth.addBorder(yor);
        nth.addBorder(lon);
        nth.addBorder(bel);
        nth.addBorder(hol);
        nth.addBorder(den);
        nth.addBorder(hel);
        nth.addBorder(ska);
        nth.addBorder(eng);
        ska.addBorder(den);
        ska.addBorder(nwy);
        ska.addBorder(swe);
        ska.addBorder(bal);
        bal.addBorder(kie);
        bal.addBorder(den);
        bal.addBorder(ber);
        bal.addBorder(pru);
        bal.addBorder(lvn);
        bal.addBorder(swe);
        bal.addBorder(bot);
        bot.addBorder(lvn);
        bot.addBorder(stp);
        bot.addBorder(fin);
        bot.addBorder(swe);
        hel.addBorder(den);
        hel.addBorder(kie);
        hel.addBorder(hol);
        eng.addBorder(bel);
        eng.addBorder(pic);
        eng.addBorder(bre);
        eng.addBorder(mid);
        eng.addBorder(iri);
        eng.addBorder(wal);
        eng.addBorder(lon);
        iri.addBorder(mid);
        iri.addBorder(nat);
        iri.addBorder(lvp);
        iri.addBorder(wal);
        nat.addBorder(cly);
        nat.addBorder(lvp);
        nat.addBorder(mid);
        mid.addBorder(bre);
        mid.addBorder(gas);
        mid.addBorder(spa);
        mid.addBorder(por);
        mid.addBorder(naf);
        mid.addBorder(wes);
        wes.addBorder(naf);
        wes.addBorder(tun);
        wes.addBorder(tyn);
        wes.addBorder(gol);
        wes.addBorder(spa);
        gol.addBorder(spa);
        gol.addBorder(mar);
        gol.addBorder(pie);
        gol.addBorder(tus);
        gol.addBorder(tyn);
        tyn.addBorder(tus);
        tyn.addBorder(rom);
        tyn.addBorder(nap);
        tyn.addBorder(ion);
        tyn.addBorder(tun);
        ion.addBorder(nap);
        ion.addBorder(apu);
        ion.addBorder(adr);
        ion.addBorder(alb);
        ion.addBorder(gre);
        ion.addBorder(aeg);
        ion.addBorder(tun);
        adr.addBorder(ven);
        adr.addBorder(tri);
        adr.addBorder(alb);
        adr.addBorder(apu);
        aeg.addBorder(gre);
        aeg.addBorder(bul);
        aeg.addBorder(con);
        aeg.addBorder(bla);
        aeg.addBorder(smy);
        aeg.addBorder(eas);
        eas.addBorder(smy);
        eas.addBorder(syr);
        bla.addBorder(con);
        bla.addBorder(bul);
        bla.addBorder(rum);
        bla.addBorder(sev);
        bla.addBorder(arm);
        bla.addBorder(ank);

    }

    public void createUnit(Main.unitType unit, Province province) {
        Unit troop = null;

        System.out.println(province.getX());

        switch (unit) {
            case ARMY:
                troop = new Army(province);
                break;
            case FLEET:
                troop = new Fleet(province);
                break;
        }

        //province.addUnit(troop);

        moveUnit(troop, province.getX(), province.getY());

        //Render troepen
        troops.getChildren().add(troop);

        /*
        troop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                Unit thisUnit = (Unit) event.getTarget();

                ArrayList<Province> borderProvinces = thisUnit.province.getBorders();
                System.out.println("clicked on province " + thisUnit.province.getName());
                for(Province province: borderProvinces){
                    System.out.println("Border province: "+province.getName());
                }
            }
        });
         */

    }

    public void moveUnit(Unit unit, double x, double y) {
        unit.setX(x);
        unit.setY(y);
    }

}
