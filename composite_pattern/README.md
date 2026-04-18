# Composite Pattern - File System Implementation

## Overview

This project demonstrates the **Composite Design Pattern** through a hierarchical file system implementation. The pattern allows you to compose objects into tree structures to represent part-whole hierarchies, letting clients treat individual objects and compositions of objects uniformly.

## Design Pattern

### Composite Pattern
The Composite Pattern is a structural pattern that allows you to compose objects into tree structures to represent part-whole hierarchies. It lets clients treat individual objects and compositions of objects uniformly.

**Key Components:**
- **Component (IFileSystem)**: Declares an interface for all components, including composite ones
- **Leaf (File)**: Represents leaf objects (terminal nodes) with no children
- **Composite (Folder)**: Can contain children and delegates operations to them

## Architecture

```
IFileSystem (Interface)
    ├── File (Leaf)
    │   └── Implements all operations
    └── Folder (Composite)
        ├── Contains List<IFileSystem>
        └── Recursively delegates operations
```

## File Structure

```
composite_pattern/
├── main.java                 # Entry point with demo
├── filesystem/
│   ├── IFileSystem.java      # Component interface
│   ├── File.java             # Leaf component
│   └── Folder.java           # Composite component
└── README.md                 # This file
```

## Core Classes

### IFileSystem Interface
Defines operations for both files and folders:
- `ls(int indent)` - List contents
- `openAll(int indent)` - Open/display all recursively
- `getSize()` - Get size (file size or total folder size)
- `cd(String name)` - Navigate to folder
- `getName()` - Get name
- `isFolder()` - Check if it's a folder

### File Class
Represents a leaf node in the tree:
- Stores file name and size
- Most operations return simple values
- `cd()` returns null (can't navigate into a file)

### Folder Class
Represents a composite node:
- Contains a list of IFileSystem children
- Operations are delegated recursively to children
- Can contain both files and folders

## UML Class Diagram

```
┌─────────────────────┐
│   IFileSystem       │
├─────────────────────┤
│ + ls(indent)        │
│ + openAll(indent)   │
│ + getSize()         │
│ + cd(name)          │
│ + getName()         │
│ + isFolder()        │
└──────────┬──────────┘
           △
           │ implements
      ┌────┴─────┐
      │          │
 ┌────┴─────┐ ┌──┴────────┐
 │   File   │ │  Folder   │
 ├──────────┤ ├───────────┤
 │ -name    │ │ -name     │
 │ -size    │ │ -children*│
 ├──────────┤ ├───────────┤
 │ +File()  │ │ +Folder() │
 └──────────┘ └───────────┘
```

## Usage Example

```java
// Create file system
List<IFileSystem> rootChildren = new ArrayList<>();
rootChildren.add(new File("file1.txt", 1));
rootChildren.add(new File("file2.txt", 1));

Folder root = new Folder("root", rootChildren);

List<IFileSystem> docsChildren = new ArrayList<>();
docsChildren.add(new File("resume.pdf", 1));
docsChildren.add(new File("notes.txt", 1));

Folder docs = new Folder("docs", docsChildren);
rootChildren.add(docs);

// Display file system
root.ls(0);        // List all items
root.openAll(0);   // Display tree structure
System.out.println(root.getSize());  // Total size

// Navigate
IFileSystem cwd = root.cd("docs");
if (cwd != null) {
    cwd.ls(0);
}
```

## Key Operations

### ls(indent)
Lists immediate children with indentation:
```
root
 file1.txt
 file2.txt
 docs/
```

### openAll(indent)
Recursively displays entire tree:
```
+ root
    file1.txt
    file2.txt
    + docs
        resume.pdf
        notes.txt
```

### getSize()
Returns total size:
- **File**: Returns file size
- **Folder**: Returns sum of all children's sizes (recursive)

### cd(name)
Navigates to child folder:
- **File**: Returns null (can't enter)
- **Folder**: Searches children and returns matching folder

## Benefits of Composite Pattern

1. **Uniform Interface**: Treat files and folders the same way
2. **Recursive Composition**: Build complex structures
3. **Simple Client Code**: No need to check types
4. **Easy to Extend**: Add new file system types easily
5. **Tree Operations**: Apply operations recursively to entire tree

## Common Use Cases

- File Systems (this project)
- GUI Components (windows containing widgets)
- Organization hierarchies
- Menu systems
- Document structures (sections, subsections)

## Running the Project

1. Compile: `javac composite_pattern/*.java composite_pattern/filesystem/*.java`
2. Run: `java composite_pattern.Main`
3. Output shows file system listing, tree structure, and total size

## Design Principles Applied

- **Single Responsibility**: Each class has one purpose
- **Open/Closed**: Easy to add new file system types
- **Liskov Substitution**: Both File and Folder can be used wherever IFileSystem is expected
- **Dependency Inversion**: Classes depend on IFileSystem abstraction, not concrete types
