package domains;

public class Fleet extends Unit{

    public Fleet(Province province) {
        super("/Fleet-" + province.getOwner().getName().toString() + ".png", province);
    }
}
