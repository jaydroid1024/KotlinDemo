package org.jay.kotlindemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import javax.inject.Inject

/**
 * Created by jay on 2017/9/13.
 */
//类与对象
//在 Kotlin 中的一个类可以有一个主构造函数和一个或多个次构造函数。
//主构造函数是类头的一部分：它跟在类名（和可选的类型参数）后。
//如果主构造函数没有任何 注解或者可见性修饰符，可以省略这个 constructor 关键字。
open class Kotlin4类与对象 constructor(name: String, context: Context, list: List<String>) {
    //主构造函数不能包含任何的代码。初始化的代码可以放到以 init 关键字作为前缀的初始化块（initializer blocks）中：
    init {
        Log.d("jay", "Customer initialized with value ${name}")
    }

    //注意，主构造的参数可以在初始化块中使用。它们也可以在类体内声明的属性初始化器中使用：
    val customerKey = name.toUpperCase()

    //事实上，声明属性以及从主构造函数初始化属性，Kotlin 有简洁的语法：
    //与普通属性一样，主构造函数中声明的属性可以是可变的（var）或只读的（val）。
    class Person(val firstName: String, val lastName: String, var age: Int) {
        // ……
    }

    //如果构造函数有 注解 或 可见性修饰符，这个 constructor 关键字是必需的，并且这些修饰符在它前面：
    class Customer public @Inject constructor(name: String) {
        //……
    }

    //次构造函数
    //类也可以声明前缀有 constructor的次构造函数：
    class Person1 constructor(name: String) {
        //如果类有一个主构造函数，每个次构造函数需要委托给主构造函数，
        // 可以直接委托或者通过别的次构造函数间接委托。
        // 委托到同一个类的另一个构造函数用 this 关键字即可：
        constructor(name: String, age: Int) : this(name) {
            Log.d("jay", name.toString())
        }
    }

    //注意 Kotlin 并没有 new 关键字。
    //创建类的实例
    //要创建一个类的实例，我们就像普通函数一样调用构造函数：
    val customer = Customer("Joe Smith")

    //继承
    //在 Kotlin 中所有类都有一个共同的超类 Any，这对于没有超类型声明的类是默认超类：
    class Example // 从 Any 隐式继承

    class Example1 : Any()
    //要声明一个显式的超类型，我们把类型放到类头的冒号之后：
    open class Base(p: Int)

    class Derived(p: Int) : Base(p)
    //如果该类有一个主构造函数，其基类型可以（并且必须） 用（基类型的）主构造函数参数就地初始化。
    //如果类没有主构造函数，那么每个次构造函数必须使用 super 关键字初始化其基类型，或委托给另一个构造函数做到这一点。
    // 注意，在这种情况下，不同的次构造函数可以调用基类型的不同的构造函数：
    //类上的 open 标注与 Java 中 final 相反，它允许其他类从这个类继承。
    //默认情况下，在 Kotlin 中所有的类都是 final，
    class MyView : View {
        constructor(ctx: Context) : super(ctx)

        constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
    }


    //覆盖方法
    //我们之前提到过，Kotlin 力求清晰显式。与 Java 不同，Kotlin 需要显式标注可覆盖的成员（我们称之为开放）和覆盖后的成员：
    open class Base1 {
        open fun v() {}
        fun nv() {}
    }

    class Derived1() : Base1() {
        override fun v() {}
    }

    //标记为 override 的成员本身是开放的，也就是说，它可以在子类中覆盖。如果你想禁止再次覆盖，使用 final 关键字：
    class AnotherDerived() : Base1() {
        final override fun v() {}
    }


    //覆盖属性
    //属性覆盖与方法覆盖类似；在超类中声明然后在派生类中重新声明的属性必须以 override 开头，并且它们必须具有兼容的类型。
    // 每个声明的属性可以由具有初始化器的属性或者具有 getter 方法的属性覆盖。
    open class Foo {
        open val x: Int = 2
    }

    class Bar1 : Foo() {
        override val x: Int = 1
    }

    //你也可以用一个 var 属性覆盖一个 val 属性，但反之则不行。
    // 这是允许的，因为一个 val 属性本质上声明了一个 getter 方法，而将其覆盖为 var 只是在子类中额外声明一个 setter 方法。
    //请注意，你可以在主构造函数中使用 override 关键字作为属性声明的一部分。
    interface Foo1 {
        val count: Int
    }

    class Bar2(override val count: Int) : Foo1
    class Bar3 : Foo1 {
        override var count: Int = 0
    }

    //调用超类实现
    //派生类中的代码可以使用 super 关键字调用其超类的函数与属性访问器的实现：
    open class Fo {
        open fun f() {
            println("Foo.f()")
        }

        open val x: Int get() = 1
    }

    class Ba : Fo() {
        override fun f() {
            super.f()
            println("Bar.f()")
        }

        override val x: Int get() = super.x + 1
    }

    //在一个内部类中访问外部类的超类，可以通过由外部类名限定的 super 关键字来实现：super@Outer：
    class Ba1 : Fo() {
        override fun f() { /* …… */
        }

        override val x: Int get() = 0

        inner class Baz {
            fun g() {
                super@Ba1.f() // 调用 Foo 实现的 f()
                println(super@Ba1.x) // 使用 Foo 实现的 x 的 getter
            }
        }
    }

    //覆盖规则
    //在 Kotlin 中，实现继承由下述规则规定：如果一个类从它的直接超类继承相同成员的多个实现，
    // 它必须覆盖这个成员并提供其自己的实现（也许用继承来的其中之一）。
    // 为了表示采用从哪个超类型继承的实现，我们使用由尖括号中超类型名限定的 super，如 super<Base>：
    open class A {
        open fun f() {
            print("A")
        }

        fun a() {
            print("a")
        }
    }

    interface B {
        fun f() {
            print("B")
        } // 接口成员默认就是“open”的

        fun b() {
            print("b")
        }
    }

    open class B1 {
        fun f() {
            print("B1")
        }

        fun b1() {
            print("b1")
        }
    }

    //同时继承 A 和 B 没问题，并且 a() 和 b() 也没问题因为 C 只继承了每个函数的一个实现。
    // 但是 f() 由 C 继承了两个实现，所以我们必须在 C 中覆盖 f() 并且提供我们自己的实现来消除歧义。
    class C() : A(), B {
        // 编译器要求覆盖 f()：
        override fun f() {
            super<A>.f() // 调用 A.f()
            super<B>.f() // 调用 B.f()
        }
    }

    //

    //抽象类
    //类和其中的某些成员可以声明为 abstract。 抽象成员在本类中可以不用实现。
    // 需要注意的是，我们并不需要用 open 标注一个抽象类或者函数——因为这不言而喻。
    //我们可以用一个抽象成员覆盖一个非抽象的开放成员
    open class Base2 {
        open fun f() {}
    }

    abstract class Derived3 : Base2() {
        override abstract fun f()
    }

    class Derived4 : Derived3() {
        override fun f() {}

    }
}