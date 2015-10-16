package mylast

import shapeless._

































trait MyLast[HL <: HList] extends DepFn1[HL]

































object MyLast {
    type Aux[H0 <: HList, T0] = MyLast[H0] {
      type Out = T0
    }

    def apply[HL <: HList](implicit ml: MyLast[HL]): Aux[HL, ml.Out] = ml

    implicit class PimpMyLast[HL <: HList, O](h : HL)(implicit ml: MyLast.Aux[HL, O]) {
      def myLast: O = ml.apply(h)
    }































    implicit def oneElement[T]: Aux[T :: HNil, T] = new MyLast[T :: HNil] {
      type Out = T
      def apply(in: T :: HNil): T = in.head
    }

    implicit def moreThanOne[H, T <: HList](implicit next: MyLast[T]): Aux[H :: T, next.Out] = new MyLast[H :: T] {
      type Out = next.Out
      def apply(in: H :: T) = next(in.tail)
    }
}
