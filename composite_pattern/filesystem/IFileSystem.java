package composite_pattern.filesystem;

public interface IFileSystem {
    void ls(int indent);
    void openAll();
    int getSize();
    IFileSystem cd (String name);
    String getName();
    boolean isFolder();
}
