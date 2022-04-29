package com.nyanyajaguar.dicepoker


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    var p1 = 0  //fun roll1 진행한 횟수(위의 roll 버튼)
    var p2 = 0  //fun roll2 진행한 횟수(아래의 roll 버튼)

    //위의 주사위 1~5에 대한 변수 n1~n10
    var n1 : Double = 1.0
    var n2 = n1.toInt()
    var n3 : Double = 1.0
    var n4 = n3.toInt()
    var n5 : Double = 1.0
    var n6 = n5.toInt()
    var n7 : Double = 1.0
    var n8 = n7.toInt()
    var n9 : Double = 1.0
    var n10 = n9.toInt()

    //아래의 주사위 6~10에 대한 변수 n11~n20
    var n11 : Double = 1.0
    var n12 = n11.toInt()
    var n13 : Double = 1.0
    var n14 = n13.toInt()
    var n15 : Double = 1.0
    var n16 = n15.toInt()
    var n17 : Double = 1.0
    var n18 = n17.toInt()
    var n19 : Double = 1.0
    var n20 = n19.toInt()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }



    // 위의 roll 버튼.
    fun roll(view:View) {


        // 처음 누를때는(p1=0일때는) 주사위 1,2 만 바뀜(n1~n4만 바뀜)
        if(p1==0 && p2 == 0) {
            n1 = (Math.random() * 6) + 1
            n2 = n1.toInt()

            n3 = (Math.random() * 6) + 1
            n4 = n3.toInt()

            dice1.set_number(n2)
            dice2.set_number(n4)

            textView2.text = "" + (n2)
            textView3.text = "" + (n4)

            p1 = p1 + 1

        }

        //두번쨰 누를때는(p1=1일때는) 주사위 3,4,5 만 바뀜(n5~n10만 바뀜)
        else if(p1==1 && p2 == 1) {

            n5 = (Math.random() * 6) + 1
            n6 = n5.toInt()

            n7 = (Math.random() * 6) + 1
            n8 = n7.toInt()

            n9 = (Math.random() * 6) + 1
            n10 = n9.toInt()

            dice3.set_number(n6)
            dice4.set_number(n8)
            dice5.set_number(n10)

            textView4.text = "" + (n6)
            textView5.text = "" + (n8)
            textView6.text = "" + (n10)

            p1 = p1 + 1

            // 두번째까지 하고나면 주사위 1~5가 전부 바꼈으므로 주사위 5개에 대한 패가 표시되도록 함
            if((n2==n4&&n2!=n6&&n2!=n8&&n2!=n10&&n6!=n8&&n6!=n10&&n8!=n10) ||
                    (n2==n6&&n2!=n4&&n2!=n8&&n2!=n10&&n4!=n8&&n4!=n10&&n8!=n10) ||
                    (n2==n8&&n2!=n6&&n2!=n4&&n2!=n10&&n4!=n6&&n4!=n10&&n6!=n10) ||
                    (n2==n10&&n2!=n6&&n2!=n8&&n2!=n4&&n4!=n8&&n4!=n6&&n6!=n8) ||

                    (n4==n6&&n4!=n2&&n4!=n8&&n4!=n10&&n2!=n8&&n2!=n10&&n8!=n10) ||
                    (n4==n8&&n4!=n6&&n4!=n2&&n4!=n10&&n2!=n6&&n2!=n10&&n6!=n10) ||
                    (n4==n10&&n4!=n6&&n4!=n8&&n4!=n2&&n2!=n6&&n2!=n8&&n6!=n8) ||

                    (n6==n8&&n6!=n4&&n6!=n2&&n6!=n10&&n2!=n4&&n2!=n10&&n4!=n10) ||
                    (n6==n10&&n6!=n4&&n6!=n8&&n6!=n2&&n2!=n4&&n2!=n8&&n4!=n8) ||

                    (n8==n10&&n8!=n4&&n8!=n6&&n8!=n2&&n2!=n4&&n2!=n6&&n4!=n6)) {

                textView.text = "One Pair"}

            else if((n2==n4&&n2!=n6&&n2!=n10&&n6!=n10&&n6==n8) ||
                    (n2==n4&&n2!=n6&&n2!=n8&&n6==n10&&n6!=n8) ||
                    (n2==n4&&n2!=n6&&n2!=n8&&n6!=n8&&n8==n10) ||

                    (n2==n6&&n2!=n4&&n2!=n10&&n4!=n10&&n4==n8) ||
                    (n2==n6&&n2!=n4&&n2!=n8&&n4!=n8&&n4==n10) ||
                    (n2==n6&&n2!=n4&&n2!=n8&&n4!=n8&&n8==n10) ||

                    (n2==n8&&n2!=n4&&n2!=n10&&n4!=n10&&n4==n6) ||
                    (n2==n8&&n2!=n4&&n2!=n6&&n4!=n6&&n4==n10) ||
                    (n2==n8&&n2!=n4&&n2!=n6&&n4!=n6&&n6==n10)||

                    (n2==n10&&n2!=n4&&n2!=n8&&n4!=n8&&n4==n6)||
                    (n2==n10&&n2!=n4&&n2!=n6&&n4!=n6&&n4==n8)||
                    (n2==n10&&n2!=n4&&n2!=n6&&n4!=n6&&n6==n8)||

                    (n4==n6&&n4!=n2&&n4!=n8&&n2!=n8&&n8==n10)||
                    (n4==n8&&n4!=n2&&n4!=n6&&n2!=n6&&n6==n10)||
                    (n4==n10&&n4!=n2&&n4!=n6&&n2!=n6&&n6==n8)) {
                textView.text = "Two Pair"}


            else if((n2==n4&&n2==n6&&n2!=n8&&n2!=n10&&n8!=n10)||
                    (n2==n4&&n2==n8&&n2!=n6&&n2!=n10&&n6!=n10)||
                    (n2==n4&&n2==n10&&n2!=n6&&n2!=n8&&n6!=n8)||

                    (n2==n6&&n2==n8&&n2!=n4&&n2!=n10&&n4!=n10)||
                    (n2==n6&&n2==n10&&n2!=n4&&n2!=n8&&n4!=n8)||
                    (n2==n8&&n2==n10&&n2!=n4&&n2!=n6&&n4!=n6)||

                    (n4==n6&&n4==n8&&n4!=n2&&n4!=n10&&n2!=n10)||
                    (n4==n6&&n4==n10&&n4!=n2&&n4!=n8&&n2!=n8)||
                    (n4==n8&&n4==n10&&n4!=n2&&n4!=n6&&n2!=n6)||

                    (n6==n8&&n6==n10&&n6!=n2&&n6!=n4&&n2!=n4)) {

                textView.text = "Triple"
            }

            else if((n2==n4&&n2==n6&&n2!=n8&&n2!=n10&&n8==n10)||
                    (n2==n4&&n2==n8&&n2!=n6&&n2!=n10&&n6==n10)||
                    (n2==n4&&n2==n10&&n2!=n6&&n2!=n8&&n6==n8)||

                    (n2==n6&&n2==n8&&n2!=n4&&n2!=n10&&n4==n10)||
                    (n2==n6&&n2==n10&&n2!=n4&&n2!=n8&&n4==n8)||
                    (n2==n8&&n2==n10&&n2!=n4&&n2!=n6&&n4==n6)||

                    (n4==n6&&n4==n8&&n4!=n2&&n4!=n10&&n2==n10)||
                    (n4==n6&&n4==n10&&n4!=n2&&n4!=n8&&n2==n8)||
                    (n4==n8&&n4==n10&&n4!=n2&&n4!=n6&&n2==n6)||

                    (n6==n8&&n6==n10&&n6!=n2&&n6!=n4&&n2==n4)) {

                textView.text = "Full House"
            }


            else if((n2==n4&&n2==n6&&n2==n8&&n2!=n10) ||
                    (n2==n4&&n2==n6&&n2==n8&&n2!=n10) ||
                    (n2==n4&&n2==n6&&n2!=n8&&n2==n10) ||
                    (n2==n4&&n2!=n6&&n2==n8&&n2==n10) ||
                    (n2!=n4&&n2==n6&&n2==n8&&n2==n10) ||
                    (n2!=n4&&n4==n6&&n4==n8&&n4==n10)) {

                textView.text = "Four Dice"
            }

            else if (n2==n4&&n2==n6&&n2==n8&&n2==n10) {

                textView.text = "King of Poker"
            }

            else if (n2!=n4&&n2!=n6&&n2!=n8&&n2!=n10&&n4!=n6&&n4!=n8&&n4!=n10&&n6!=n8&&n6!=n10&&n8!=n10&&n2+n4+n6+n8+n10==15) {

                textView.text = "Straight"
            }

            else if (n2!=n4&&n2!=n6&&n2!=n8&&n2!=n10&&n4!=n6&&n4!=n8&&n4!=n10&&n6!=n8&&n6!=n10&&n8!=n10&&n2+n4+n6+n8+n10==20) {

                textView.text = "Royal Straight"
            }


            else{ textView.text = "nothing" }




        }





    }

    fun roll2(view:View) {


        if(p1==1 && p2 == 0) {
            n11 = (Math.random() * 6) + 1
            var n12 = n11.toInt()

            n13 = (Math.random() * 6) + 1
            var n14 = n13.toInt()

            view6.set_number(n12)
            view7.set_number(n14)

            textView14.text = "" + (n12)
            textView15.text = "" + (n14)

            p2 = p2 + 1

        }

        else if(p1== 2 && p2== 1) {

            n15 = (Math.random() * 6) + 1
            n16 = n15.toInt()

            n17 = (Math.random() * 6) + 1
            n18 = n17.toInt()

            n19 = (Math.random() * 6) + 1
            n20 = n19.toInt()

            view8.set_number(n16)
            view9.set_number(n18)
            view10.set_number(n20)

            textView16.text = "" + (n16)
            textView17.text = "" + (n18)
            textView18.text = "" + (n20)

            p2 = p2 + 1

            if((n12==n14&&n12!=n16&&n12!=n18&&n12!=n20&&n16!=n18&&n16!=n20&&n18!=n20) ||
                    (n12==n16&&n12!=n14&&n12!=n18&&n12!=n20&&n14!=n18&&n14!=n20&&n18!=n20) ||
                    (n12==n18&&n12!=n16&&n12!=n14&&n12!=n20&&n14!=n16&&n14!=n20&&n16!=n20) ||
                    (n12==n20&&n12!=n16&&n12!=n18&&n12!=n14&&n14!=n18&&n14!=n16&&n16!=n18) ||

                    (n14==n16&&n14!=n12&&n14!=n18&&n14!=n20&&n12!=n18&&n12!=n20&&n18!=n20) ||
                    (n14==n18&&n14!=n16&&n14!=n12&&n14!=n20&&n12!=n16&&n12!=n20&&n16!=n20) ||
                    (n14==n20&&n14!=n16&&n14!=n18&&n14!=n12&&n12!=n16&&n12!=n18&&n16!=n18) ||

                    (n16==n18&&n16!=n14&&n16!=n12&&n16!=n20&&n12!=n14&&n12!=n20&&n14!=n20) ||
                    (n16==n20&&n16!=n14&&n16!=n18&&n16!=n12&&n12!=n14&&n12!=n18&&n14!=n18) ||

                    (n18==n20&&n18!=n14&&n18!=n16&&n18!=n12&&n12!=n14&&n12!=n16&&n14!=n16)) {

                textView13.text = "One Pair"}

            else if((n12==n14&&n12!=n16&&n12!=n20&&n16!=n20&&n16==n18) ||
                    (n12==n14&&n12!=n16&&n12!=n18&&n16==n20&&n16!=n18) ||
                    (n12==n14&&n12!=n16&&n12!=n18&&n16!=n18&&n18==n20) ||

                    (n12==n16&&n12!=n14&&n12!=n20&&n14!=n20&&n14==n18) ||
                    (n12==n16&&n12!=n14&&n12!=n18&&n14!=n18&&n14==n20) ||
                    (n12==n16&&n12!=n14&&n12!=n18&&n14!=n18&&n18==n20) ||

                    (n12==n18&&n12!=n14&&n12!=n20&&n14!=n20&&n14==n16) ||
                    (n12==n18&&n12!=n14&&n12!=n16&&n14!=n16&&n14==n20) ||
                    (n12==n18&&n12!=n14&&n12!=n16&&n14!=n16&&n16==n20)||

                    (n12==n20&&n12!=n14&&n12!=n18&&n14!=n18&&n14==n16)||
                    (n12==n20&&n12!=n14&&n12!=n16&&n14!=n16&&n14==n18)||
                    (n12==n20&&n12!=n14&&n12!=n16&&n14!=n16&&n16==n18)||

                    (n14==n16&&n14!=n12&&n14!=n18&&n12!=n18&&n18==n20)||
                    (n14==n18&&n14!=n12&&n14!=n16&&n12!=n16&&n16==n20)||
                    (n14==n20&&n14!=n12&&n14!=n16&&n12!=n16&&n16==n18)) {
                textView13.text = "Two Pair"}


            else if((n12==n14&&n12==n16&&n12!=n18&&n12!=n20&&n18!=n20)||
                    (n12==n14&&n12==n18&&n12!=n16&&n12!=n20&&n16!=n20)||
                    (n12==n14&&n12==n20&&n12!=n16&&n12!=n18&&n16!=n18)||

                    (n12==n16&&n12==n18&&n12!=n14&&n12!=n20&&n14!=n20)||
                    (n12==n16&&n12==n20&&n12!=n14&&n12!=n18&&n14!=n18)||
                    (n12==n18&&n12==n20&&n12!=n14&&n12!=n16&&n14!=n16)||

                    (n14==n16&&n14==n18&&n14!=n12&&n14!=n20&&n12!=n20)||
                    (n14==n16&&n14==n20&&n14!=n12&&n14!=n18&&n12!=n18)||
                    (n14==n18&&n14==n20&&n14!=n12&&n14!=n16&&n12!=n16)||

                    (n16==n18&&n16==n20&&n16!=n12&&n16!=n14&&n12!=n14)) {

                textView13.text = "Triple"
            }

            else if((n12==n14&&n12==n16&&n12!=n18&&n12!=n20&&n18==n20)||
                    (n12==n14&&n12==n18&&n12!=n16&&n12!=n20&&n16==n20)||
                    (n12==n14&&n12==n20&&n12!=n16&&n12!=n18&&n16==n18)||

                    (n12==n16&&n12==n18&&n12!=n14&&n12!=n20&&n14==n20)||
                    (n12==n16&&n12==n20&&n12!=n14&&n12!=n18&&n14==n18)||
                    (n12==n18&&n12==n20&&n12!=n14&&n12!=n16&&n14==n16)||

                    (n14==n16&&n14==n18&&n14!=n12&&n14!=n20&&n12==n20)||
                    (n14==n16&&n14==n20&&n14!=n12&&n14!=n18&&n12==n18)||
                    (n14==n18&&n14==n20&&n14!=n12&&n14!=n16&&n12==n16)||

                    (n16==n18&&n16==n20&&n16!=n12&&n16!=n14&&n12==n14)) {

                textView13.text = "Full House"
            }


            else if((n12==n14&&n12==n16&&n12==n18&&n12!=n20) ||
                    (n12==n14&&n12==n16&&n12==n18&&n12!=n20) ||
                    (n12==n14&&n12==n16&&n12!=n18&&n12==n20) ||
                    (n12==n14&&n12!=n16&&n12==n18&&n12==n20) ||
                    (n12!=n14&&n12==n16&&n12==n18&&n12==n20) ||
                    (n12!=n14&&n14==n16&&n14==n18&&n14==n20)) {

                textView13.text = "Four Dice"
            }

            else if (n12==n14&&n12==n16&&n12==n18&&n12==n20) {

                textView13.text = "King of Poker"
            }

            else if (n12!=n14&&n12!=n16&&n12!=n18&&n12!=n20&&n14!=n16&&n14!=n18&&n14!=n20&&n16!=n18&&n16!=n20&&n18!=n20&&n12+n14+n16+n18+n20==15) {

                textView13.text = "Straight"
            }

            else if (n12!=n14&&n12!=n16&&n12!=n18&&n12!=n20&&n14!=n16&&n14!=n18&&n14!=n20&&n16!=n18&&n16!=n20&&n18!=n20&&n12+n14+n16+n18+n20==20) {

                textView13.text = "Royal Straight"
            }


            else{ textView13.text = "nothing" }




        }

    }


}
