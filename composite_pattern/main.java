package composite_pattern;

import java.util.List;
import java.util.ArrayList;

import composite_pattern.filesystem.File;
import composite_pattern.filesystem.Folder;
import composite_pattern.filesystem.IFileSystem;

class Main {
    
    public static void main (String[] args) {

        // Build file system
        List<IFileSystem> rootChildren = new ArrayList<>();
        rootChildren.add(new File("file1.txt", 1));
        rootChildren.add(new File("file2.txt", 1));
        
        Folder root = new Folder("root", rootChildren);

        List<IFileSystem> docsChildren = new ArrayList<>();
        docsChildren.add(new File("resume.pdf", 1));
        docsChildren.add(new File("notes.txt", 1));
        
        Folder docs = new Folder("docs", docsChildren);
        root.cd("docs");
        rootChildren.add(docs);

        List<IFileSystem> imagesChildren = new ArrayList<>();
        imagesChildren.add(new File("photo.jpg", 1));
        
        Folder images = new Folder("images", imagesChildren);
        rootChildren.add(images);

        root.ls(0);

        docs.ls(0);

        root.openAll(0);

        IFileSystem cwd = root.cd("docs");
        if (cwd != null) {
            cwd.ls(0);
        } else {
            System.out.println("\nCould not cd into docs\n");
        }

        System.out.println(root.getSize());
    }
}
