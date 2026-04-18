package composite_pattern.filesystem;

import java.util.List;

public class Folder implements IFileSystem {

    private String name;
    private List<IFileSystem> children;

    public Folder(String name, List<IFileSystem> children) {
        this.name = name;
        this.children = children;
    }

    @Override
    public void ls(int indent) {
        String indentSpaces = " ".repeat(indent);

        for (IFileSystem child : children) {
            if (child.isFolder()) {
                System.out.println(indentSpaces + child.getName() + "/");
            } else {
                System.out.println(indentSpaces + child.getName());
            }
        }
    }

    @Override
    public void openAll(int indent) {

        String indentSpaces = " ".repeat(indent);

        System.out.println(indentSpaces + "+ " + name);

        for (IFileSystem child: children) child.openAll(indent + 4);
    }

    @Override
    public int getSize() {
        int totalSize = 0;

        for (IFileSystem child: children) totalSize += child.getSize();

        return totalSize;
    }


    @Override
    public IFileSystem cd(String target) {

        for (IFileSystem child: children) 
            if (child.isFolder() && child.getName().equals(target)) 
                return child;

        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isFolder() {
        return true;
    }
    
}
