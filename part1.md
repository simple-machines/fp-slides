# Functional and type-level programming

# Part 1

## Typeclasses

---

# Typeclasses???

- First introduced by Haskell
- Contraversial feature, turned out to be awesome
- Amazing generality while maintaining simple code
- Scala accidentally got them through parameterised traits and implicit conversions.
  - Some actual syntax support added later, since they turned out so useful.

---

# Ad-hoc polymorphism

In the java world, polymorphism is supported through class and interface inheritance. Scala continues this through traits.

```scala
trait MakesSound {
  def sound: String
}

class Pig extends Animal with MakesSound {
  def sound = "OINK!"
}
```
This is cool when you control all the types involved, but what if you don't???

---

# Ad-hoc polymorphism

For example, java's `hashCode()` returns an `Int`. `hashCode` lives on `Object` instead of having its' own interface. Retarded, but nevermind, that is the java legacy.

What if we wanted a hash that was a `Long`.
```scala
trait Hashable {
	def hash: Long
}
```

We can implement it for our types:

```scala
class Animal extends Hashable {
   def hash = ???
}
```

---

# Ad-hoc polymorphism

Now we can add it to our fancy hash set:

```scala
class FancyHashSet[T <: Hashable] {
  def add(t: T) = ???
  ...
}

val set = new FancyHashSet[Animal]
set.add(new Animal)
```

---

#Ad-hoc polymorphism

## What if we wanted to add a String?

It doesn't really work, because `String` is not ours, and we cannot add traits to its' hierarchy.

---
# Typeclases to the rescue!!!

```scala
trait Hashable[T] {
	def hash(t: T): Long
}

implicit object StringHashable extends Hashable[String] {
	def hash(t: String) = ???
}
```

Now our hypothetical Hash Set will look like:

```scala
class HashSet[T](implicit hashable: Hashable[T]) {
	def add(t: T) {
	  val tHash: Long = hashable.hash(t)
	  ???
	}
}
```
We can only construct a `HashSet[T]` if we have an implicit `Hashable[T]` available.


Hashable is the **typeclass**, StringHashable is the **typeclass instance**.

---

# Typeclasses to the rescue

There is special syntax for the previous code:

```scala
class HashSet[T : Hashable] {
    def add(t: T) {
      val tHash: Long = implicitly[Hashable[T]].hash(t)
      ???
    }
}
```

Which desugars into the generic version.

Typeclasses allow for generic functional programming in a way that is more powerful than straight inheritance. We will see examples of this later.

---

# Pimping

So far we have seen that we can call `hash` on all `T` that have hashable instances by implicitly getting the instance:

```scala
val str = "Apple"
val hash = implicitly[Hashable[String]].hash(str)
```
Which works but is not pretty. We want:

```
"Apple".hash
```
And we can!!!

---

# Pimping

```scala
implicit class HashableSyntax[T : Hashable](t:T) {
   def hash = implicitly[Hashable[T]].hash(t)
}
```

This implicit conversion gives us a decorator. So we can now if we have an instance of `Hashable` and the `HashableSyntax` visible through imports, we can use `hash` as if it were a method on `String` (or any other type).

This is cool!!!

---

# Some examples

`scala.math.Ordering` in the scala standard library.

`play.api.libs.json.Format` in the Play framework.

Other traits with a single type parameter everywhere!!!

### Next week: the big four!!!
