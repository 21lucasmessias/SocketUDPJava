public class Main {
    public static void main(String[] args) {
        try {
            final Bar bar = new Bar(
                    5,
                    2,
                    2,
                    2
            );
            bar.initializeBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
