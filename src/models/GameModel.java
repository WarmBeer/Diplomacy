package models;

import controllers.GameController;
import domains.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import observers.GameViewObservable;
import observers.GameViewObserver;
import observers.OrderObservable;
import observers.OrderObserver;
import utilities.KeyHandler;
import views.GameView;

import java.util.ArrayList;
import java.util.List;

import static application.Main.unitType;

public class GameModel implements Model, OrderObservable, GameViewObservable {

    private Group troops;
    private Group points;

    public enum Countries {
        FRANCE,
        GERMANY,
        ENGLAND,
        AUSTRIA,
        TURKEY,
        RUSSIA,
        ITALY,
        INDEPENDENT
    }

    private GameView gameView;
    private Game activeGame;
    private Game lobbyGame;
    ArrayList<OrderObserver> viewObservers = new ArrayList<>();
    ArrayList<GameViewObserver> gameViewObservers = new ArrayList<>();
    private boolean pointsChanged = false;
    private boolean hasComboBoxes = false;
    private boolean removeVisualPoints = false;
    private boolean disableOrderMenu = false;
    private ArrayList<String> provComboBoxValues;

    public GameModel(Stage stage, GameController gameController) {
        this.gameView = new GameView(stage, gameController);
    }

    public void show() {
        this.removeVisualPoints = false;
        this.gameView.init();
    }
    
    public Game getActiveGame() {
        return  this.activeGame;
    }

    public void changedComboBox(String action, Province selectedProvince, ComboBox provComboBox) {
        provComboBoxValues = new ArrayList<>();

        hasComboBoxes = true;
        Player provinceLeader = selectedProvince.getOwner().getLeader();
        boolean isThisClient = provinceLeader.isThisLocalPlayer();

        //the player slected a province that isn't theirs
        if(!isThisClient) {
            setDisableOrderMenu(true);
        } else {
            setDisableOrderMenu(false);
            if(!action.equals("Hold")){
                for(Province province : selectedProvince.getBorders()) {
                    provComboBoxValues.add(province.getName());
                }
            }
            if(action.equals("Hold") || action.equals("Action")) {
                provComboBoxValues.add("Select Province");
            }
        }

        notifyGameViewObservers();
    }

    @FXML
    public void initGame(GameJSON gameJSON) {
        Game game = new Game(gameJSON.gameUID, gameJSON.name, gameJSON.turnTime, gameJSON.turn);
        points = new Group();
        troops = new Group();
        initProvinces(game);

        Country independent = new Country(Countries.INDEPENDENT);
        game.addCountry(independent);

        for(Player player : gameJSON.Players) {
            game.addPlayer(player);

            Country country = new Country(player.getCountry());
            country.setPlayer(player);
            game.addCountry(country);
        }

        for (ProvinceJSON provinceJSON : gameJSON.Provinces) {
            for (Province province : game.getProvinces()) {
                if (province.getAbbreviation().equals(provinceJSON.abbr)) {


                    for(Country country : game.getCountries()) {
                        if(provinceJSON.owner == country.getName()) {
                            province.setOwner(country);
                        }
                    }


                    Unit stationed = null;

                    if (province.getOwner() != null && provinceJSON.stationed != null) {
                        UnitJSON unitJSON = provinceJSON.stationed;
                        switch (provinceJSON.stationed.unitType) {
                            case ARMY:
                                stationed = new Army(province);
                                break;
                            case FLEET:
                                stationed = new Fleet(province);
                                break;
                        }


                        Province target = null;
                        for (int i = 0;i<game.getProvinces().size();i++) {
                            if(game.getProvinces().get(i).getAbbreviation().equals(unitJSON.orderTarget)) {
                                target = game.getProvinces().get(i);
                            }
                        }
                        stationed.addOrder(unitJSON.orderType, target);
                        createUnit(provinceJSON.stationed.unitType, province);
                    }

                    province.addUnit(stationed);
                }
            }
        }

        //Maak troepen aan
        for(Province province: game.getProvinces()){
            points.getChildren().add(province);

            if (province.getOwner() == null) {
                province.setOwner(independent);
            }

            switch(province.getProvinceType()){
                case SEA:
                    province.setImage(new Image("Point coastal.png"));
                    break;
                case LAND:
                    province.setImage(new Image("Point.png"));
                    break;
            }
        }


        this.activeGame = game;
        this.setPointsChanged();
        this.notifyGameViewObservers();

    }

    public void playerJoined(String uid, String name, Countries owner) {
        if (this.lobbyGame.getPlayers().size() < 7) {
            Player player = new Player();
            player.setId(this.lobbyGame.getPlayers().size());
            player.setCountry(owner);
            player.setName(name);
            player.setUID(uid);

            this.lobbyGame.addPlayer(player);

            Country country = new Country(player.getCountry());
            country.setPlayer(player);
            this.lobbyGame.addCountry(country);
        }
    }

    @FXML
    public void createLobby(String gameName, int turnTime) {
        String gameUID = KeyHandler.generateKey(8);
        Game game = new Game(gameUID, gameName, turnTime, 1);

        this.lobbyGame = game;
    }

    public void startLobby() {
        points = new Group();
        troops = new Group();
        initProvinces(this.lobbyGame);

        Country independent = new Country(Countries.INDEPENDENT);
        this.lobbyGame.addCountry(independent);

        for(Player player : this.lobbyGame.getPlayers()) {
            Country country = new Country(player.getCountry());
            country.setPlayer(player);
            this.lobbyGame.addCountry(country);

            for (int i = 0;i < this.lobbyGame.getProvinces().size();i++) {
                if (this.lobbyGame.getProvinces().get(i).getCountry().equals(player.getCountry())) {
                    this.lobbyGame.getProvinces().get(i).setOwner(country);
                }
            }
        }

        //Maak troepen aan
        for(Province province: this.lobbyGame.getProvinces()){
            points.getChildren().add(province);

            if (province.getOwner() == null) {
                province.setOwner(independent);
            }

            switch(province.getProvinceType()){
                case SEA:
                    province.setImage(new Image("Point coastal.png"));
                    break;
                case LAND:
                    province.setImage(new Image("Point.png"));
                    break;
            }
        }

        createUnitsPerPlayer();


        this.activeGame = this.lobbyGame;
        this.lobbyGame = null;
        this.setPointsChanged();
        this.notifyGameViewObservers();
    }

    public void initProvinces(Game game) {
        //----GERMANY//----
        Province kie = new Province("Kiel", "kie", true, 580, 535, Countries.GERMANY);
        game.addProvince(kie);
        Province ber = new Province("Berlin", "ber", true, 655, 505, Countries.GERMANY);
        game.addProvince(ber);
        Province pru = new Province("Prussia", "pru", false, 738, 500, Countries.GERMANY);
        game.addProvince(pru);
        Province ruh = new Province("Ruhr", "ruh", false, 540, 600, Countries.GERMANY);
        game.addProvince(ruh);
        Province mun = new Province("Munich", "mun", true, 603, 585, Countries.GERMANY);
        game.addProvince(mun);
        Province sil = new Province("silesia", "sil", false, 725, 541, Countries.GERMANY);
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

        Province pic = new Province("Picardy", "pic", false, 435, 565, Countries.FRANCE);
        game.addProvince(pic);
        Province bre = new Province("Brest", "bre", true, 330, 630, Countries.FRANCE);
        game.addProvince(bre);
        Province par = new Province("Paris", "par", true, 390, 655, Countries.FRANCE);
        game.addProvince(par);
        Province bur = new Province("Burgundy", "bur", false, 475, 605, Countries.FRANCE);
        game.addProvince(bur);
        Province gas = new Province("Gascony", "gas", false, 340, 725, Countries.FRANCE);
        game.addProvince(gas);
        Province mar = new Province("Marseilles", "mar", true, 465, 715, Countries.FRANCE);
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
        Province pie = new Province("piedmont", "pie", false, 540, 705, Countries.ITALY);
        game.addProvince(pie);
        Province ven = new Province("Venice", "ven", true, 624, 734, Countries.ITALY);
        game.addProvince(ven);
        Province tus = new Province("Tuscany", "tus", false, 607, 776, Countries.ITALY);
        game.addProvince(tus);
        Province rom = new Province("Rome", "rom", true, 650, 811, Countries.ITALY);
        game.addProvince(rom);
        Province apu = new Province("Apulia", "apu", false, 748, 829, Countries.ITALY);
        game.addProvince(apu);
        Province nap = new Province("Naples", "nap", true, 740, 869, Countries.ITALY);
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
        Province boh = new Province("Bohemia", "boh", false, 698, 625, Countries.AUSTRIA);
        game.addProvince(boh);
        Province gal = new Province("Galicia", "gal", false, 907, 618, Countries.AUSTRIA);
        game.addProvince(gal);
        Province tyr = new Province("Tyrolia", "tyr", false, 667, 650, Countries.AUSTRIA);
        game.addProvince(tyr);
        Province vie = new Province("Vienna", "vie", true, 748, 689, Countries.AUSTRIA);
        game.addProvince(vie);
        Province bud = new Province("Budapest", "bud", true, 846, 691, Countries.AUSTRIA);
        game.addProvince(bud);
        Province tri = new Province("Trieste", "tri", true, 707, 715, Countries.AUSTRIA);
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
        Province rum = new Province("Rumania", "rum", true, 960, 738);
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
        Province con = new Province("Constantinople", "con", true, 1030, 844, Countries.TURKEY);
        game.addProvince(con);
        Province smy = new Province("Smyrna", "smy", true, 1071, 880, Countries.TURKEY);
        game.addProvince(smy);
        Province ank = new Province("Ankara", "ank", true, 1175, 818, Countries.TURKEY);
        game.addProvince(ank);
        Province arm = new Province("Armenia", "arm", false, 1339, 827, Countries.TURKEY);
        game.addProvince(arm);
        Province syr = new Province("Syria", "syr", false, 1342, 945, Countries.TURKEY);
        game.addProvince(syr);


        con.addBorder(ank);
        con.addBorder(smy);
        smy.addBorder(ank);
        smy.addBorder(arm);
        smy.addBorder(syr);
        ank.addBorder(arm);
        arm.addBorder(syr);


        //-----ENGLANG//---//---//---//---
        Province cly = new Province("Clyde", "cly", false, 325, 400, Countries.ENGLAND);
        game.addProvince(cly);
        Province edi = new Province("Edinburgh", "edi", true, 372, 380, Countries.ENGLAND);
        game.addProvince(edi);
        Province lvp = new Province("Liverpool", "lvp", true, 359, 455, Countries.ENGLAND);
        game.addProvince(lvp);
        Province yor = new Province("York", "yor", false, 390, 490, Countries.ENGLAND);
        game.addProvince(yor);
        Province wal = new Province("Wales", "wal", false, 300, 515, Countries.ENGLAND);
        game.addProvince(wal);
        Province lon = new Province("London", "lon", true, 395, 515, Countries.ENGLAND);
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

        Province war = new Province("Warsaw", "war", true, 857, 552, Countries.RUSSIA);
        game.addProvince(war);
        Province lvn = new Province("Livonia", "lvn", false, 937, 430, Countries.RUSSIA);
        game.addProvince(lvn);
        Province urk = new Province("Urkaine", "urk", false, 1060, 590, Countries.RUSSIA);
        game.addProvince(urk);
        Province mos = new Province("Moscow", "mos", true, 1244, 412, Countries.RUSSIA);
        game.addProvince(mos);
        Province stp = new Province("St. Petersburg", "stp", true, 1048, 344, Countries.RUSSIA);
        game.addProvince(stp);
        Province fin = new Province("Finland", "fin", false, 883, 290, Countries.RUSSIA);
        game.addProvince(fin);
        Province sev = new Province("Sevastopol", "sev", true, 1384, 637, Countries.RUSSIA);
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
        Province por = new Province("Portugal", "por", true, 90, 815);
        game.addProvince(por);
        Province naf = new Province("N. Africa", "naf", false,200, 965);
        game.addProvince(naf);
        Province tun = new Province("Tunis", "tun", true, 563, 944);
        game.addProvince(tun);


        spa.addBorder(por);
        naf.addBorder(tun);


        //-----NORWAY, SWEDEN DENMARK------

        Province nwy = new Province("Norway", "nwy", true, 595, 325);
        game.addProvince(nwy);
        Province swe = new Province("Sweden", "swe", true, 690, 360);
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
        Province iri = new Province("Irish Sea", "iri", false, 240, 515, Province.ProvinceType.SEA);
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

        //zet province image attributes
        for(Province province: game.getProvinces()) {
            province.setTranslateX(-25);
            province.setTranslateY(-35);

            province.setScaleX(0.2);
            province.setScaleY(0.2);

            province.setX(province.getX());
            province.setY(province.getY());

            province.setOpacity(0.6);
        }
    }

    /**
     * Give each player an unit to their supply center
     */
    public void createUnitsPerPlayer() {
        for(Province province : getProvinces()) {
            for(Player player : activeGame.getPlayers()){
                if(province.getCountry() == player.getCountry()) {
                    if(province.isSupplyCenter()) {
                        createUnit(unitType.ARMY, province);

                    }
                }
                   // System.out.println(province.getName() + " - " + province.getOwner().getName());
            }
        }
//        for(Player player : this.activeGame.getPlayers()) {
//
//            Country playerCountry = player.getCountry();
//           // playerCountry.
//        }
    }

    public void createUnit(unitType unit, Province province) {
        Unit troop = null;

        switch (unit) {
            case ARMY:
                troop = new Army(province);
                break;
            case FLEET:
                troop = new Fleet(province);
                break;
        }

        province.addUnit(troop);
        troops.getChildren().add(troop);
        moveUnit(troop, province.getX(), province.getY());
    }

    public void moveUnit(Unit unit, double x, double y) {
        unit.setX(x);
        unit.setY(y);
    }

    // If a Unit already has an order, remove it from Orderlist.
    public void checkDuplicateUnitOrder(ListView lvOrders, String newOrder) {
        String[] data = newOrder.split("_");
        String province = data[1];
        Object toRemove = null;
        for (Object order : lvOrders.getItems()) {
            if (order.toString().split("_")[1].equals(province)) {
                System.out.println("Duplicate order found for Unit, removing order.");
                toRemove = order;
            }
        }
        if (toRemove != null) { lvOrders.getItems().remove(toRemove); }
    }

    @Override
    public void registerOrderObserver(OrderObserver orderobserver) {
        viewObservers.add(orderobserver);
    }

    @Override
    public void unregisterOrderObserver(OrderObserver orderobserver) {
        viewObservers.remove(orderobserver);
    }

    @Override
    public void registerGameViewObserver(GameViewObserver gameViewObserver) {
        gameViewObservers.add(gameViewObserver);
    }

    @Override
    public void unregisterGameViewObserver(GameViewObserver gameViewObserver) {
        gameViewObservers.remove(gameViewObserver);
    }

    @Override
    public void notifyGameViewObservers() {
        for(GameViewObserver gameViewObserver : gameViewObservers) {
            gameViewObserver.update(this);
        }
    }

    @Override
    public void notifyOrderObservers() {
        for(OrderObserver gameViewObserver : viewObservers) {
            gameViewObserver.update(this);
        }
    }

    @Override
    public List<Province> getProvinces() {
        return this.activeGame.getProvinces();
    }

    @Override
    public Group getTroopsGroup() {
        return this.troops;
    }

    @Override
    public Group getPointsGroup() {
        return this.points;
    }

    private void setPointsChanged() {
        this.pointsChanged = true;
    }

    @Override
    public boolean pointsChanged() {
        pointsChanged = !pointsChanged;
        return !pointsChanged;
    }

    @Override
    public boolean hasComboBoxes() {
        if(hasComboBoxes){
            hasComboBoxes = false;
            return true;
        }
        return false;

    }

    public void hideVisualPoints() {
        removeVisualPoints = true;
        notifyGameViewObservers();
    }

    public void showVisualPoints() {
        removeVisualPoints = false;
        notifyGameViewObservers();
    }

    @Override
    public boolean doRemoveAllPoints() {
        return removeVisualPoints;
    }

    public void setDisableOrderMenu(boolean disableOrderMenu) {
        this.disableOrderMenu = disableOrderMenu;
    }

    @Override
    public boolean getDisableOrderMenu() {
        return disableOrderMenu;
    }

    @Override
    public ArrayList<String> getOrderList() {
        return orderList;
    }


    ArrayList<String> orderList = new ArrayList<String>();

    public void addOrder(String action, String prov1, String prov2){
        String order = action + "_" + prov1 + "_" + prov2;
        orderList.add(order);
        notifyOrderObservers();
    }

    public ArrayList<String> getComboBox1Values() {
        return this.provComboBoxValues;
    }

    public Province getProvinceFromAbbr(String provinceAbbr) {
        for(Province province : getProvinces()) {
            if(province.getAbbreviation().equals(provinceAbbr))
                return province;
        }
        return null;
    }

    public Province getProvinceFromName(String provinceName) {
        for(int i = 0;i < getProvinces().size();i++) {
            if(getProvinces().get(i).getName().equals(provinceName))
                return getProvinces().get(i);
        }
        return null;
    }

}