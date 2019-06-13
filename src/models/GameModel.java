package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domains.Game;
import domains.Province;
import domains.Unit;
import observers.GameObservable;
import observers.GameObserver;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class GameModel implements GameObservable {

    public final String GAME_VIEW = "/resources/views/GameView.fxml"; //DIT MOET WEG UITEINDELIJK!!!

    public enum Countries {
        FRANCE,
        GERMANY,
        ENGLAND,
        AUSTRIA,
        TURKEY,
        RUSSIA,
        ITALY
    }

    private Game currentGame;
    ArrayList<GameObserver> viewObservers = new ArrayList<GameObserver>();



//    public void initGame(GameJSON gameJSON) {
//
//        Game game = new Game(gameJSON.gameUID, gameJSON.name, gameJSON.turnTime, gameJSON.turn);
//
//        initProvinces(game);
//
//        for(Player player : gameJSON.Players) {
//            game.addPlayer(player);
//
//            Country country = new Country(player.getCountry());
//            game.addCountry(country);
//        }
//
//        for (ProvinceJSON provinceJSON : gameJSON.Provinces) {
//            for (Province province : game.getProvinces()) {
//                if (province.getAbbreviation().equals(provinceJSON.abbr)) {
//
//                    for(Country country : game.getCountries()) {
//                        if(provinceJSON.owner == country.getName()) {
//                            province.setOwner(country);
//                        }
//                    }
//
//                    Unit stationed = null;
//
//                    if (province.getOwner() != null) {
//                        switch (provinceJSON.stationed) {
//                            case ARMY:
//                                stationed = new Army(province);
//                                break;
//                            case FLEET:
//                                stationed = new Fleet(province);
//                                break;
//                        }
//                    }
//
//                    if (stationed != null) {
//                        createUnit(provinceJSON.stationed, province);
//                    }
//
//                    province.addUnit(stationed);
//                }
//            }
//        }
//
//        //Maak troepen aan
//        for(Province province: game.getProvinces()){
//
//
//            switch(province.getProvinceType()){
//                case SEA:
//                    province.setImage(new Image("Point coastal.png"));
//                    //createUnit(unitType.Fleet, province, province.getX(), province.getY());
//                    break;
//                case LAND:
//                    province.setImage(new Image("Point.png"));
//                    //createUnit(unitType.Army, province, province.getX(), province.getY());
//                    break;
//            }
//
//            province.setTranslateX(-25);
//            province.setTranslateY(-35);
//
//            province.setScaleX(0.2);
//            province.setScaleY(0.2);
//
//            province.setX(province.getX());
//            province.setY(province.getY());
//
//            province.setOpacity(0.6);
//
//            province.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                public void handle(MouseEvent event) {
//                    Province provincePoint = (Province ) event.getTarget();
//
//                    for(Node node : points.getChildren()) {
//                        Province nodeProvince = (Province) node;
//                        nodeProvince.setOpacity(0.6);
//                        nodeProvince.setScaleX(0.2);
//                        nodeProvince.setScaleY(0.2);
//                    }
//
//                    provincePoint.setOpacity(1);
//                    provincePoint.setScaleX(0.4);
//                    provincePoint.setScaleY(0.4);
//                }
//            });
//
//            points.getChildren().add(province);
//
//        }
//
//        this.currentGame = game;
//
//    }

    public void initProvinces(Game game) {
        //^([a-z]{3})\s'([^']+)'
        //Province $1 = new Province\("$2", "$1"\);

        //(Province )([a-z]{3})([^;]+;\r\n);
        //$1$2$3\t\tprovinces.add\($2\);\r\n


        //normal to code
        //\t([a-z]{3})\s'([^']+)'\s([0-9]+)\s([0-9]+);
        //\tProvince $1 = new Province\("$2", "$1"\, $3, $4\);\r\n\t\tprovinces.add\($1\);

        //star to setSupplycenter
        //\t\*\s([a-z]{3});
        //\t($1).setIsSupplyCenter\(true\);

        //----GERMANY//----
        Province kie = new Province("Kiel", "kie", true, 580, 520);
        game.addProvince(kie);
        Province ber = new Province("Berlin", "ber", true, 655, 485);
        game.addProvince(ber);
        Province pru = new Province("Prussia", "pru", false, 738, 480);
        game.addProvince(pru);
        Province ruh = new Province("Ruhr", "ruh", false, 540, 580);
        game.addProvince(ruh);
        Province mun = new Province("Munich", "mun", true, 603, 585);
        game.addProvince(mun);
        Province sil = new Province("silesia", "sil", false, 715, 530);
        game.addProvince(sil);

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

        Province pic = new Province("Picardy", "pic", false, 435, 565);
        game.addProvince(pic);
        Province bre = new Province("Brest", "bre", true, 330, 615);
        game.addProvince(bre);
        Province par = new Province("Paris", "par", true, 390, 640);
        game.addProvince(par);
        Province bur = new Province("Burgundy", "bur", false, 475, 605);
        game.addProvince(bur);
        Province gas = new Province("Gascony", "gas", false, 335, 705);
        game.addProvince(gas);
        Province mar = new Province("Marseilles", "mar", true, 465, 715);
        game.addProvince(mar);


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
        Province pie = new Province("piedmont", "pie", false, 540, 705);
        game.addProvince(pie);
        Province ven = new Province("Venice", "ven", true, 624, 734);
        game.addProvince(ven);
        Province tus = new Province("Tuscany", "tus", false, 607, 776);
        game.addProvince(tus);
        Province rom = new Province("Rome", "rom", true, 650, 811);
        game.addProvince(rom);
        Province apu = new Province("Apulia", "apu", false, 748, 829);
        game.addProvince(apu);
        Province nap = new Province("Naples", "nap", true, 731, 864);
        game.addProvince(nap);


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
        game.addProvince(boh);
        Province gal = new Province("Galicia", "gal", false, 907, 618);
        game.addProvince(gal);
        Province tyr = new Province("Tyrolia", "tyr", false, 667, 650);
        game.addProvince(tyr);
        Province vie = new Province("Vienna", "vie", true, 748, 689);
        game.addProvince(vie);
        Province bud = new Province("Budapest", "bud", true, 846, 691);
        game.addProvince(bud);
        Province tri = new Province("Trieste", "tri", true, 707, 715);
        game.addProvince(tri);


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
        game.addProvince(rum);
        Province ser = new Province("Serbia", "ser", true, 870, 805);
        game.addProvince(ser);
        Province alb = new Province("Albania", "alb", false, 838, 840);
        game.addProvince(alb);
        Province bul = new Province("Bulgaria", "bul", true, 941, 799);
        game.addProvince(bul);
        Province gre = new Province("Greece", "gre", false, 879, 845);
        game.addProvince(gre);


        rum.addBorder(ser);
        rum.addBorder(bul);
        ser.addBorder(bul);
        ser.addBorder(alb);
        ser.addBorder(gre);
        alb.addBorder(gre);
        bul.addBorder(gre);


        //-----TURKEY//---//---
        Province con = new Province("Constantinople", "con", true, 1030, 844);
        game.addProvince(con);
        Province smy = new Province("Smyrna", "smy", true, 1071, 880);
        game.addProvince(smy);
        Province ank = new Province("Ankara", "ank", true, 1175, 818);
        game.addProvince(ank);
        Province arm = new Province("Armenia", "arm", false, 1339, 827);
        game.addProvince(arm);
        Province syr = new Province("Syria", "syr", false, 1342, 945);
        game.addProvince(syr);


        con.addBorder(ank);
        con.addBorder(smy);
        smy.addBorder(ank);
        smy.addBorder(arm);
        smy.addBorder(syr);
        ank.addBorder(arm);
        arm.addBorder(syr);


        //-----ENGLANG//---//---//---//---
        Province cly = new Province("Clyde", "cly", false, 325, 385);
        game.addProvince(cly);
        Province edi = new Province("Edinburgh", "edi", true, 372, 380);
        game.addProvince(edi);
        Province lvp = new Province("Liverpool", "lvp", true, 359, 455);
        game.addProvince(lvp);
        Province yor = new Province("York", "yor", false, 390, 475);
        game.addProvince(yor);
        Province wal = new Province("Wales", "wal", false, 300, 515);
        game.addProvince(wal);
        Province lon = new Province("London", "lon", true, 395, 515);
        game.addProvince(lon);

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
        game.addProvince(war);
        Province lvn = new Province("Livonia", "lvn", false, 937, 430);
        game.addProvince(lvn);
        Province urk = new Province("Urkaine", "urk", false, 1060, 590);
        game.addProvince(urk);
        Province mos = new Province("Moscow", "mos", true, 1244, 412);
        Province stp = new Province("St. Petersburg", "stp", true, 1048, 344);
        game.addProvince(mos);
        game.addProvince(stp);
        Province fin = new Province("Finland", "fin", false, 883, 290);
        game.addProvince(fin);
        Province sev = new Province("Sevastopol", "sev", true, 1384, 637);
        game.addProvince(sev);


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

        Province spa = new Province("Spain", "spa", true, 204, 830);
        game.addProvince(spa);
        Province por = new Province("Portugal", "por", true, 80, 800);
        game.addProvince(por);
        Province naf = new Province("N. Africa", "naf", false,200, 965);
        game.addProvince(naf);
        Province tun = new Province("Tunis", "tun", true, 563, 944);
        game.addProvince(tun);


        spa.addBorder(por);
        naf.addBorder(tun);


        //-----NORWAY, SWEDEN DENMARK------

        Province nwy = new Province("Norway", "nwy", true, 595, 310);
        game.addProvince(nwy);
        Province swe = new Province("Sweden", "swe", true, 690, 345);
        game.addProvince(swe);
        Province den = new Province("Denmark", "den", true, 600, 418);
        game.addProvince(den);


        nwy.addBorder(swe);


        //----independent provinces------

        Province hol = new Province("Holland", "hol", true, 507, 516);
        game.addProvince(hol);
        Province bel = new Province("Belgium", "bel", true, 491, 553);
        game.addProvince(bel);

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
        Province bar = new Province("Barents Sea", "bar", false, 850, 10, Province.ProvinceType.SEA);
        game.addProvince(bar);
        Province nrg = new Province("NRG Norwegian Sea", "nrg", false, 524, 175, Province.ProvinceType.SEA);
        game.addProvince(nrg);
        Province nth = new Province("NTH North Sea", "nth", false, 475, 445, Province.ProvinceType.SEA);
        game.addProvince(nth);
        Province ska = new Province("SKA", "ska", false, 640, 390, Province.ProvinceType.SEA);
        game.addProvince(ska);
        Province bal = new Province("Baltic Sea", "bal", false, 790, 440, Province.ProvinceType.SEA);
        game.addProvince(bal);
        Province bot = new Province("BOT Gulf of Botnia", "bot", false, 785, 300, Province.ProvinceType.SEA);
        game.addProvince(bot);
        Province hel = new Province("HEL Helgoland Bight", "hel", false, 535, 470, Province.ProvinceType.SEA);
        game.addProvince(hel);
        Province eng = new Province("English channel", "eng", false, 365, 555, Province.ProvinceType.SEA);
        game.addProvince(eng);
        Province iri = new Province("Irish Sea", "iri", false, 230, 500, Province.ProvinceType.SEA);
        game.addProvince(iri);
        Province nat = new Province("N. Atlantic", "nat", false, 135, 343, Province.ProvinceType.SEA);
        game.addProvince(nat);
        Province mid = new Province("Mid Atlantic", "mid", false, 99, 655, Province.ProvinceType.SEA);
        game.addProvince(mid);
        Province wes = new Province("West Mediterranean", "wes", false, 414, 870, Province.ProvinceType.SEA);
        game.addProvince(wes);
        Province gol = new Province("GOL Golf of Lyon", "gol", false, 465, 820, Province.ProvinceType.SEA);
        game.addProvince(gol);
        Province tyn = new Province("Tyrhennian Sea", "tyn", false, 590, 890, Province.ProvinceType.SEA);
        game.addProvince(tyn);
        Province ion = new Province("Ionian Sea", "ion", false, 789, 920, Province.ProvinceType.SEA);
        game.addProvince(ion);
        Province adr = new Province("Adriantic Sea", "adr", false, 765, 800, Province.ProvinceType.SEA);
        game.addProvince(adr);
        Province aeg = new Province("Aegean Sea", "aeg", false, 965, 920, Province.ProvinceType.SEA);
        game.addProvince(aeg);
        Province eas = new Province("East Mid", "eas", false, 1165, 930, Province.ProvinceType.SEA);
        game.addProvince(eas);
        Province bla = new Province("Black Sea", "bla", false, 1100, 760, Province.ProvinceType.SEA);
        game.addProvince(bla);


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

//    public void createUnit(Main.unitType unit, Province province) {
//        Unit troop = null;
//
//        switch (unit) {
//            case ARMY:
//                troop = new Army(province);
//                break;
//            case FLEET:
//                troop = new Fleet(province);
//                break;
//        }
//
//        province.addUnit(troop);
//
//        moveUnit(troop, province.getX(), province.getY());
//
//        //Render troepen
//        troops.getChildren().add(troop);
//
//        troop.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent event) {
//
//                Unit thisUnit = (Unit) event.getTarget();
//
//                ArrayList<Province> borderProvinces = thisUnit.province.getBorders();
//                System.out.println("clicked on province " + thisUnit.province.getName());
//                for(Province province: borderProvinces){
//                    System.out.println("Border province: "+province.getName());
//                }
//            }
//        });
//
//    }

    public void moveUnit(Unit unit, double x, double y) {
        unit.setX(x);
        unit.setY(y);
    }

    public void loadGame() throws Exception{
        Reader reader = new FileReader("/Diplomacy.json");
        Gson gson = new GsonBuilder().create();
        Game p = gson.fromJson(reader, Game.class);
        System.out.println(p);
    }


    @Override
    public void registerObserver(GameObserver gameobserver) {
        viewObservers.add(gameobserver);
    }

    @Override
    public void unregisterObserver(GameObserver gameobserver) {
        viewObservers.remove(gameobserver);
    }

    @Override
    public void notifyObservers() {
        for(GameObserver gameobserver : viewObservers) {
            gameobserver.update(this);
        }
    }

    @Override
    public ArrayList<String> getOrderList() {
        return orderList;
    }

    ArrayList<String> orderList = new ArrayList<String>();

    public void addOrder(String action, String prov1, String prov2){
        String order = action + "_" + prov1 + "_" + prov2;
        orderList.add(order);
        notifyObservers();
    }

}
