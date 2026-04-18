package composite_pattern.filesystem;

public class File implements IFileSystem{

    private String name;
    private int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }
    @Override
    public void ls(int indent) {
        String indentSpace = " ".repeat(indent);
        System.out.println(indentSpace + name);
    }

    @Override
    public void openAll(int indent) {
        String indentSpaces = " ".repeat(indent);
        System.out.println(indentSpaces + name);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public IFileSystem cd(String name) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isFolder() {
        return false;
    }
    
}
