# Functional and type-level programming

# Part 2

## Monoids

---

# Recap

- Typeclasses allow us to define generic interfaces for our types.
- Allows us to generically work with the typeclass without knowing the concrete type.
- Ad-hoc in the sense that we don't need to explicitly `extend/with` the trait.

---

# The big four

- `Monoid`
- `Functor`
- `Applicative`
- `Monad`

For these, I usually use `scalaz` but we will be exploring the concepts without `scalaz`. My method names may be different but the structure and concepts are identical. Please don't roll your own just because you can!!!

---

# Monoid

```scala
trait Monoid[T] {
	def mempty: T
	def mappend(a: T, b: T): T
}
```

BTW, this is a typeclass.

## Laws

```scala
//Mempty is zero-ish
mappend(mempty, x) = x
mappend(x, mempty) = x

//Associativity
mappend(x, mappend(y, z)) = mappend(mappend(x y), z)
```

So monoids are things we can add, append, compose or concatenate (and the order doesn't matter), with an empty element.

Typeclasses often have laws. Scalacheck can check them.

---

# Monoid
## Examples
### String
```scala
implicit object StringMonoid extends Monoid[String] {
  def mempty = ""
  def mappend(a: String, b: String) = a + b
}
```

---

# Monoid
## Examples
### Int (+)
```scala
implicit object AddIntMonoid extends Monoid[Int] {
  def mempty = 0
  def mappend(a: Int, b: Int) = a + b
}
```

---

# Monoid
## Examples
### Int (\*)

```scala
implicit object MultIntMonoid extends Monoid[Int] {
  def mempty = 1
  def mappend(a: Int, b: Int) = a * b
}
```

---

# Monoid
## Examples
### More interesting: Functions!!!


```scala
implicit class FuncMonoid[A] extends Monoid[A => A] {
  def mempty = (a: A) => a
  def mappend(a: A => A, b: A => A) = a.andThen(b)
}
```

---
# Monoid
## Examples

### Examples are everywhere

- Graphics - matrices
- Version control commits
- State-modifying events
- Lists, sets, maps

If you can, write generic code!

---

# Why monoids???
## Fold/reduce
```scala
def reduce[A : Monoid](as: Seq[A]): A = {
    val monoid = implicitly[Monoid[A]]
	as.foldLeft(monoid.mempty)(monoid.mappend)
}

```

- Monoids by definition are exactly the parameters `foldLeft` takes. They are the generic version of things that can be folded across.
- Moreso, because of the associativity law, they can be folded in any order, including recursively, which gives us opportunities for paralellism:

```scala
def parReduce[A : Monoid](as: Seq[A]): A = {
    val monoid = implicitly[Monoid[A]]
	as.par.reduce(monoid.mappend)
}
```
What we need for parallel reduction of sequences is **exactly** monoids.
