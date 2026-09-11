public class usecase1 {
    private String title;
    private String author;
    private double price;
    private String isbn;
    static int bookCount = 0;
    static final String libraryName = "Sunrise Public Library";
    public usecase1(String title, String author, double price, String isbn) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
        bookCount++;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public double getPrice() {
        return price;
    }
    public String getIsbn() {
        return isbn;
    }
    public static void main(String[] args) {
        usecase1 book = new usecase1("Java Basics", "Shivam", 499.99, "ISBN12345");
        System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());
        System.out.println("Library: " + libraryName);
        System.out.println("Total books: " + bookCount);
    }
}