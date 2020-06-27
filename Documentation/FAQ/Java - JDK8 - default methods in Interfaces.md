## [JDK8 - default methods in Interfaces](https://www.programiz.com/java-programming/interfaces)



With the release of Java 8, methods with implementation (default methods) were introduced inside an interface. **Before that, all the methods were abstract in Java.**

To declare default methods inside interfaces, we use the `default` keyword. For example,

```
public default void getSides() {
   // body of getSides()
}
```

### Why default methods?

Let's take a scenario to understand why default methods are introduced in Java.

Suppose, we need to add a new method in an interface.

We can add the method in our interface easily without implementation. However, that's not the end of the story. All our classes that implement that interface must provide an implementation for the method.

If a large number of classes were implementing this interface, we need to track all these classes and make changes in them. This is not only tedious but error-prone as well.

To resolve this, Java introduced default methods. **Default methods are inherited like ordinary methods.**

Let’s take an example to have a better understanding of default methods.

### Example 2: Default Method

```
interface  Polygon {
   void getArea();
   default void getSides() {
      System.out.println("I can get sides of polygon.");
   }
}

class Rectangle implements Polygon {
   public void getArea() {
      int length = 6;
      int breadth = 5;
      int area = length * breadth;
      System.out.println("The area of the rectangle is "+area);
   }

   public void getSides() {
      System.out.println("I have 4 sides.");
   }
}

class Square implements Polygon {
   public void getArea() {
      int length = 5;
      int area = length * length;
      System.out.println("The area of the square is "+area);
   }
}

class Main {
   public static void main(String[] args) {
      Rectangle r1 = new Rectangle();
      r1.getArea();
      r1.getSides();

      Square s1 = new Square();
      s1.getArea();
   }
}
```

**Output**

```
The area of the rectangle is 30
I have 4 sides
The area of the square is 25
```

In the above example, we have created an interface Polygon. Polygon has a default method `getSides()` and an abstract method `getArea()`.

The class Rectangle then implements Polygon. Rectangle provides an implementation for the abstract method `getArea()` and overrides the default method `getSides()`.

We have created another class Square that also implements Polygon. Here, Square only provides an implementation for the abstract method `getArea()`.