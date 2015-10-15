# Functional and type-level programming

# Part 2

## Type tricks in scala

---

## Type parameters

```scala

trait Parameterised[T] { ... }
```

---

## Variance

### Covariance
```scala
  trait Collection[+T] {
    def head: T = ???
   }
```

### Contravariance
```scala
trait PrettyPrinter[-T] {
  def pprint(t: T): String = ???
}
```
- Rule of thumb:
  - Incoming types are contravariant
  - Outgoing types are covariant

  ```scala
    trait Function1[-A, +B] {}
  ```

---

## Higher-kinded type parameters
### Parameters with parameters

```scala
trait HigherKinded[F[_]] { //notice the gap
  def lift[A]: F[A]
}
```  
- This allows for typeclasses for parameterised types.

```scala
trait Monad[+M[_]] {
  def unit[A](a: A): M[A]
  def bind[A, B](m: M[A])(f: A => M[B]): M[B]
}

implicit object listMonad extends Monad[List] {
  def unit[A](a: A): List[A] = List(a)
  def bind[A, B](m: List[A])(f: A => List[B]): List[B] = m.flatMap(f)
}
```

---

## Type aliases

```scala
type CustomerId = String
type CouldBeError[A] = Either[String, A]


def getOrError[A]: CouldBeError[A]
```

---

## Abstract types

```scala
trait Foo {
  type A

  def doStuff: A
}

class IntFoo extends Foo {
  type A = Int
  def doStuff = 2
}

new IntFoo().doStuff

scala> new IntFoo().doStuff
res2: Int = 2

```

---

## Abstract types

- Can we write a function that takes some `Foo`s and returns
a list of their outputs?
- Sure!

```scala
  def doubleFooOutput[F <: Foo](f : F): List[F#A] =
    List(f.doStuff, f.doStuff)

val x: List[Int] = doubleFooOutput(new IntFoo())
res2: List[IntFoo#A] = List(2, 2)
```
- What can we do with those dependent types???

```scala

//WON't compile
def duplicateAndMap[B](f : Foo, fun: f.A => B): List[B] =
  List(f.doStuff, f.doStuff).map(fun)

  error: illegal dependent method type: parameter appears in the type of another parameter in the same section or an earlier one
```

---

## Abstract types
### Aux trick

```scala
  object Foo {
    type Aux[A0] = Foo {
      type A = A0
    }
  }


def duplicateAndMap[T, B](f: Foo.Aux[T], fun: T => B): List[B] =
  List(f.doStuff, f.doStuff).map(fun)

```

---
## Typed lambda

Recall :
- `A#B` refers to the type `B` from inside `A`

 ```scala
 type CouldBeError[A] = Either[String, A]

 val x: CouldBeError[A] = ???
 ```

 can be written as:

 ```scala
 val x: (type lambda[A] = Either[String, A])#lambda = ???
 ```
