import java.util.List;

public class Main {
    public static void main(String[] args) {
        KVStore store = new KVStore();

        try {
            store.put("sde_bootcamp",
                    List.of(new Pair<>("title", "SDE-Bootcamp"), new Pair<>("price", "30000.00"),
                            new Pair<>("enrolled", "false"), new Pair<>("estimated_time", "30")));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        Object value = store.get("sde_bootcamp");
        if(value != null) System.out.println(value);
        else System.out.println("No entry found for sde_bootcamp");

        List<String> keys = store.keys();
        keys.forEach(s -> System.out.println(s + " "));

        try {
            store.put("sde_kickstart",
                    List.of(new Pair<>("title", "SDE-Kickstart"), new Pair<>("price", "4000"),
                            new Pair<>("enrolled", "true"), new Pair<>("estimated_time", "8")));
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        value = store.get("sde_kickstart");
        if(value != null) System.out.println(value);
        else System.out.println("No entry found for sde_kickstart");

        keys = store.keys();
        System.out.println("keys = " + keys);

        try {
            store.put("sde_kickstart",
                    List.of(new Pair<>("title", "SDE-Kickstart"), new Pair<>("price", "4000.00"),
                            new Pair<>("enrolled", "true"), new Pair<>("estimated_time", "8")));
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        value = store.get("sde_kickstart");
        if(value != null) System.out.println(value);
        else System.out.println("No entry found for sde_kickstart");

        keys = store.keys();
        System.out.println("keys = " + keys);

        store.delete("sde_bootcamp");

        value = store.get("sde_bootcamp");
        if(value != null) System.out.println(value);
        else System.out.println("No entry found for sde_bootcamp");

        keys = store.keys();
        System.out.println("keys = " + keys);

        try {
            store.put("sde_bootcamp",
                    List.of(new Pair<>("title", "SDE-Bootcamp"), new Pair<>("price", "30000.00"),
                            new Pair<>("enrolled", "true"), new Pair<>("estimated_time", "30")));
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        keys = store.search("price", "30000.00");
        System.out.println(keys);

        keys = store.search("enrolled", "true");
        System.out.println(keys);
    }
}
