package test


trait RNG:
  def nextInt: (Int, RNG) // Should generate a random `Int`. We'll later define other functions in terms of `nextInt`.


object RNG:
  // NB - this was called SimpleRNG in the book text

  case class Simple(seed: Long) extends RNG {
    def nextInt: (Int, RNG) = 
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL // `&` is bitwise AND. We use the current seed to generate a new seed.
      val nextRNG = Simple(newSeed) // The next state, which is an `RNG` instance created from the new seed.
      val n = (newSeed >>> 16).toInt // `>>>` is right binary shift with zero fill. The value `n` is our new pseudo-random integer.
      (n, nextRNG) // The return value is a tuple containing both a pseudo-random integer and the next `RNG` state.
  }

  def nonNegativeInt(rng: RNG): (Int, RNG) = 
    val (i, r) = rng.nextInt
    (if i < 0 then -i else i, r)

  def double(rng: RNG): (Double, RNG) = 
    val (i, r) = nonNegativeInt(rng)
    (i / (Int.MaxValue.toDouble + 1), r)

  def intDouble(rng: RNG): ((Int, Double), RNG) = 
    val (i, r0) = rng.nextInt
    val (d, r1) = double(r0)
    ((i, d), r1)
  
  def doubleInt(rng: RNG): ((Double, Int), RNG) = 
    val ((i, d), r) = intDouble(rng)
    ((d, i), r)
  
  def double3(rng: RNG): ((Double, Double, Double), RNG) = 
    val (d1, r1) = double(rng)
    val (d2, r2) = double(r1)
    val (d3, r3) = double(r2)
    ((d1, d2, d3), r3)

  def ints(count: Int)(rng: RNG): (List[Int], RNG) = 
    if count <= 0 then (Nil, rng)
    else
      val (i, r) = rng.nextInt
      val (is, rs) = ints(count - 1)(r)
      (i :: is, rs)
  
  def lazyInts(rng: RNG): (LazyList[Int], RNG) =
    ???


  type Rand[+A] = RNG => (A, RNG)

  val int: Rand[Int] = _.nextInt

  def unit[A](a: A): Rand[A] =
    rng => (a, rng)

  def map[A, B](s: Rand[A])(f: A => B): Rand[B] = 
    rng => 
      val (a, rng2) = s(rng)
      (f(a), rng2)

  def nonNegativeEven: Rand[Int] = 
    map(nonNegativeInt)(i => i - i % 2)

  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    rng =>
      val (a, r1) = ra(rng)
      val (b, r2) = rb(r1)
      (f(a, b), r2)
  

  def both[A,B](ra: Rand[A], rb: Rand[B]): Rand[(A,B)] =
    map2(ra, rb)((_, _))

  val randIntDouble: Rand[(Int, Double)] =
    both(int, double)

  val randDoubleInt: Rand[(Double, Int)] =
    both(double, int)
  
  def sequence[A](fs: List[Rand[A]]): Rand[List[A]] = ???

  def flatMap[A, B](r: Rand[A])(f: A => Rand[B]): Rand[B] =
    rng =>
      val (a, r1) = r(rng)
      f(a)(r1)
  
  def _map[A, B](s: Rand[A])(f: A => B): Rand[B] =
    flatMap(s)(a => unit(f(a)))

  def _map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
    flatMap(ra)(a => map(rb)(b => f(a, b)))

end RNG


//import State.*
case class State[S, +A](run: S => (A, S)):

  def unit[T](a: T): State[S, T] = 
    State(s => (a, s))

  def map[B](f: A => B): State[S, B] =
    flatMap(a => unit(f(a)))

  def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] =
    flatMap(a => sb.map(b => f(a, b)))

  def flatMap[B](f: A => State[S, B]): State[S, B] = State {
    s => 
      val (a, s1) = run(s)
      f(a).run(s1)
  }

end State


object State:

  type Rand[A] = State[RNG, A]

  def unit[S, A](a: A): State[S, A] =
    State(s => (a, s))

  

  def modify[S](f: S => S): State[S, Unit] = for {
    s <- get
    _ <- set(f(s))
  } yield ()

  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))

end State