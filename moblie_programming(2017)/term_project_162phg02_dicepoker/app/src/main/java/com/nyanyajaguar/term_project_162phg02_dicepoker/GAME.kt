package com.nyanyajaguar.term_project_162phg02_dicepoker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*


class GAME : AppCompatActivity() {

    var p1 = 0  //fun roll1 진행한 횟수(위의 roll 버튼)
    var p2 = 0  //fun roll2 진행한 횟수(아래의 roll 버튼)

    var betting : String = "참가비 : "
    var betting_1 : String = "참가비 : "
    var betting2 : String = ""
    var betting2_1 : String = ""

    //각 항목 배팅한 횟수에 대한 변수
    var p1b1 = 0
    var p1b2 = 0
    var p1b3 = 0
    var p1b4 = 0
    var p1b5 = 0
    var p2b1 = 0
    var p2b2 = 0
    var p2b3 = 0
    var p2b4 = 0
    var p2b5 = 0


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

    // 최종 배팅시 주사위 선택여부 선택되어있지않으면 0, 선택되면 1
    var s1_1 = 0
    var s1_2 = 0
    var s1_3 = 0
    var s1_4 = 0
    var s1_5 = 0
    var s2_1 = 0
    var s2_2 = 0
    var s2_3 = 0
    var s2_4 = 0
    var s2_5 = 0

    var gamenotice = 0
    var buttonctrl = 0
    var ruleset = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }

    //설명창띄우기
    fun game_notice_in(view:View) {
        game_notice.setVisibility(View.VISIBLE)
        game_notice.bringToFront()
        game_notice.invalidate()
        gamenotice = gamenotice + 1
    }

    fun game_notice_out(view:View) {
        game_notice.setVisibility(View.GONE)
        gamenotice = gamenotice - 1
    }

    fun button_in(view:View) {
        button.setVisibility(View.VISIBLE)
        button.bringToFront()
        button.invalidate()
        buttonctrl = buttonctrl + 1
    }

    fun button_out(view:View) {
        button.setVisibility(View.GONE)
        buttonctrl = buttonctrl - 1
    }

    fun rule_in(view:View) {
        rule.setVisibility(View.VISIBLE)
        rule.bringToFront()
        rule.invalidate()
        ruleset = ruleset + 1
    }

    fun rule_out(view:View) {
        rule.setVisibility(View.GONE)
        ruleset = ruleset - 1
    }





    //player1이 설거지 버튼을 누르면
    fun p1_b1(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p1b1 = p1b1 + 1
            betting_1 = betting_1 + " 설거지,"
            betting2_1 = betting_1 + "\n판돈 : "
            p1_notice.text = betting_1
        }
        else if(p1==p2 && p1==1) {
            p1b1 = p1b1 + 1
            betting2_1 = betting2_1 + " 설거지,"
            p1_notice.text = betting2_1
        }
        else if(p1==p2 && p1==2) {
            p1b1 = p1b1 + 1
            betting2_1 = betting2_1 + " 설거지,"
            p1_notice.text = betting2_1
        }
        else if(p1==p2 && p1==3) {
            p1b1 = p1b1 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }

    }
    //player1이 빨래 버튼을 누르면
    fun p1_b2(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p1b2 = p1b2 + 1
            betting_1 = betting_1 + " 빨래,"
            betting2_1 = betting_1 + "\n판돈 : "
            p1_notice.text = betting_1
        }
        else if(p1==p2 && p1==1) {
            p1b2 = p1b2 + 1
            betting2_1 = betting2_1 + " 빨래,"
            p1_notice.text = betting2_1
        }
        else if(p1==p2 && p1==2) {
            p1b2 = p1b2 + 1
            betting2 = betting2 + " 빨래,"
            p1_notice.text = betting2
        }
        else if(p1==p2 && p1==3) {
            p1b2 = p1b2 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }


    }
    //player1이 청소 버튼을 누르면
    fun p1_b3(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p1b3 = p1b3 + 1
            betting_1 = betting_1 + " 청소,"
            betting2_1 = betting_1 + "\n판돈 : "
            p1_notice.text = betting_1
        }
        else if(p1==p2 && p1==1) {
            p1b3 = p1b3 + 1
            betting2_1 = betting2_1 + " 청소,"
            p1_notice.text = betting2_1
        }
        else if(p1==p2 && p1==2) {
            p1b3 = p1b3 + 1
            betting2_1 = betting2_1 + " 청소,"
            p1_notice.text = betting2
        }
        else if(p1==p2 && p1==3) {
            p1b3 = p1b3 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }


    }
    //player1이 1회권 버튼을 누르면
    fun p1_b4(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p1b4 = p1b4 + 1
            betting_1 = betting_1 + " 1회권,"
            betting2_1 = betting_1 + "\n판돈 : "
            p1_notice.text = betting_1
        }
        else if(p1==p2 && p1==1) {
            p1b4 = p1b4 + 1
            betting2_1 = betting2_1 + " 1회권,"
            p1_notice.text = betting2_1
        }
        else if(p1==p2 && p1==2) {
            p1b4 = p1b4 + 1
            betting2_1 = betting2_1 + " 1회권,"
            p1_notice.text = betting2_1
        }
        else if(p1==p2 && p1==3) {
            p1b4 = p1b4 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }

    }
    //player1이 1일권 버튼을 누르면
    fun p1_b5(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p1b5 = p1b5 + 1
            betting_1 = betting_1 + " 1일권,"
            betting2_1 = betting_1 + "\n판돈 : "
            p1_notice.text = betting_1
        }
        else if(p1==p2 && p1==1) {
            p1b5 = p1b5 + 1
            betting2_1 = betting2_1 + " 1일권,"
            p1_notice.text = betting2_1
        }
        else if(p1==p2 && p1==2) {
            p1b5 = p1b5 + 1
            betting2_1 = betting2_1 + " 1일권,"
            p1_notice.text = betting2_1
        }
        else if(p1==p2 && p1==3) {
            p1b5 = p1b5 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }


    }

    //player2이 설거지 버튼을 누르면
    fun p2_b1(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p2b1 = p2b1 + 1
            betting = betting + " 설거지,"
            betting2 = betting + "\n판돈 : "
            p2_notice.text = betting
        }
        else if(p1==p2 && p1==1) {
            p2b1 = p2b1 + 1
            betting2 = betting2 + " 설거지,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==2) {
            p2b1 = p2b1 + 1
            betting2 = betting2 + " 설거지,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==3) {
            p2b1 = p2b1 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }


    }
    //player2이 빨래 버튼을 누르면
    fun p2_b2(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p2b2 = p2b2 + 1
            betting = betting + " 빨래,"
            betting2 = betting + "\n판돈 : "
            p2_notice.text = betting
        }
        else if(p1==p2 && p1==1) {
            p2b2 = p2b2 + 1
            betting2 = betting2 + " 빨래,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==2) {
            p2b2 = p2b2 + 1
            betting2 = betting2 + " 빨래,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==3) {
            p2b2 = p2b2 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }


    }
    //player2이 청소 버튼을 누르면
    fun p2_b3(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p2b3 = p2b3 + 1
            betting = betting + " 청소,"
            betting2 = betting + "\n판돈 : "
            p2_notice.text = betting
        }
        else if(p1==p2 && p1==1) {
            p2b3 = p2b3 + 1
            betting2 = betting2 + " 청소,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==2) {
            p2b3 = p2b3 + 1
            betting2 = betting2 + " 청소,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==3) {
            p2b3 = p2b3 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }


    }
    //player2이 1회권 버튼을 누르면
    fun p2_b4(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p2b4 = p2b4 + 1
            betting = betting + " 1회권,"
            betting2 = betting + "\n판돈 : "
            p2_notice.text = betting
        }
        else if(p1==p2 && p1==1) {
            p2b4 = p2b4 + 1
            betting2 = betting2 + " 1회권,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==2) {
            p2b4 = p2b4 + 1
            betting2 = betting2 + " 1회권,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==3) {
            p2b4 = p2b4 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }


    }
    //player2이 1일권 버튼을 누르면
    fun p2_b5(view:View) {

        if(p1!=p2 && p1!=0) {
            Toast.makeText(this,"아직 !  " + name_2 + " 의 차레입니다",Toast.LENGTH_SHORT).show()
        }
        else if(p1==p2 && p1==0) {
            p2b5 = p2b5 + 1
            betting = betting + " 1일권,"
            betting2 = betting + "\n판돈 : "
            p2_notice.text = betting
        }
        else if(p1==p2 && p1==1) {
            p2b5 = p2b5 + 1
            betting2 = betting2 + " 1일권,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==2) {
            p2b5 = p2b5 + 1
            betting2 = betting2 + " 1일권,"
            p2_notice.text = betting2
        }
        else if(p1==p2 && p1==3) {
            p2b5 = p2b5 + 1
            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_SHORT).show()
        }


    }

    //포기 버튼을 누르면
    fun giveup(view:View) {

        if(p1==p2 && p1 != 3) {
            Toast.makeText(this,"" + name_2 + "  의 승리입니다 !!",Toast.LENGTH_SHORT).show()
            p1 = 3
            p2 = 3
        }
    }
    fun giveup2(view:View) {

        if(p1==p2 && p1 != 3) {
            Toast.makeText(this,"" + name_1 + "  의 승리입니다 !!",Toast.LENGTH_SHORT).show()
            p1 = 3
            p2 = 3
        }
    }

    // 주사위 선택 여부에 대한 함수
    fun p1_sel1(view:View) {
        if(p2== 2 && s1_1 == 0) {
            p1button1.setBackgroundResource(R.drawable.selectbutton)
            s1_1 = s1_1 + 1
        }
        else if(p2== 2 && s1_1 == 1) {
            p1button1.setBackgroundResource(android.R.color.transparent)
            s1_1 = s1_1 - 1
        }
    }
    fun p1_sel2(view:View) {
        if(p2== 2 && s1_2 == 0) {
            p1button2.setBackgroundResource(R.drawable.selectbutton)
            s1_2 = s1_2 + 1
        }
        else if(p2== 2 && s1_2 == 1) {
            p1button2.setBackgroundResource(android.R.color.transparent)
            s1_2 = s1_2 - 1
        }
    }
    fun p1_sel3(view:View) {
        if(p2== 2 && s1_3 == 0) {
            p1button3.setBackgroundResource(R.drawable.selectbutton)
            s1_3 = s1_3 + 1
        }
        else if(p2== 2 && s1_3 == 1) {
            p1button3.setBackgroundResource(android.R.color.transparent)
            s1_3 = s1_3 - 1
        }
    }
    fun p1_sel4(view:View) {
        if(p2== 2 && s1_4 == 0) {
            p1button4.setBackgroundResource(R.drawable.selectbutton)
            s1_4 = s1_4 + 1
        }
        else if(p2== 2 && s1_4 == 1) {
            p1button4.setBackgroundResource(android.R.color.transparent)
            s1_4 = s1_4 - 1
        }
    }
    fun p1_sel5(view:View) {
        if(p2== 2 && s1_5 == 0) {
            p1button5.setBackgroundResource(R.drawable.selectbutton)
            s1_5 = s1_5 + 1
        }
        else if(p2== 2 && s1_5 == 1) {
            p1button5.setBackgroundResource(android.R.color.transparent)
            s1_5 = s1_5 - 1
        }
    }

    fun p2_sel1(view:View) {
        if(p2== 2 && s2_1 == 0) {
            p2button1.setBackgroundResource(R.drawable.selectbutton)
            s2_1 = s2_1 + 1
        }
        else if(p2== 2 && s2_1 == 1) {
            p2button1.setBackgroundResource(android.R.color.transparent)
            s2_1 = s2_1 - 1
        }
    }
    fun p2_sel2(view:View) {
        if(p2== 2 && s2_2 == 0) {
            p2button2.setBackgroundResource(R.drawable.selectbutton)
            s2_2 = s2_2 + 1
        }
        else if(p2== 2 && s2_2 == 1) {
            p2button2.setBackgroundResource(android.R.color.transparent)
            s2_2 = s2_2 - 1
        }
    }
    fun p2_sel3(view:View) {
        if(p2== 2 && s2_3 == 0) {
            p2button3.setBackgroundResource(R.drawable.selectbutton)
            s2_3 = s2_3 + 1
        }
        else if(p2== 2 && s2_3 == 1) {
            p2button3.setBackgroundResource(android.R.color.transparent)
            s2_3 = s2_3 - 1
        }
    }
    fun p2_sel4(view:View) {
        if(p2== 2 && s2_4 == 0) {
            p2button4.setBackgroundResource(R.drawable.selectbutton)
            s2_4 = s2_4 + 1
        }
        else if(p2== 2 && s2_4 == 1) {
            p2button4.setBackgroundResource(android.R.color.transparent)
            s2_4 = s2_4 - 1
        }
    }
    fun p2_sel5(view:View) {
        if(p2== 2 && s2_5 == 0) {
            p2button5.setBackgroundResource(R.drawable.selectbutton)
            s2_5 = s2_5 + 1
        }
        else if(p2== 2 && s2_5 == 1) {
            p2button5.setBackgroundResource(android.R.color.transparent)
            s2_5 = s2_5 - 1
        }
    }


    //player1 의 주사위를 던질떄의 반응(아래 roll 버튼 눌렀을때)
    fun roll(view: View) {

        if(p1b1+p1b2+p1b3+p1b4+p1b5+p2b1+p2b2+p2b3+p2b4+p2b5 == 0) {

            Toast.makeText(this,"참가비를 먼저 걸어주세요!",Toast.LENGTH_SHORT).show()

        }

        else if((p1b1!=p2b1||p1b2!=p2b2||p1b3!=p2b3||p1b4!=p2b4||p1b5!=p2b5) && p1==0) {

            Toast.makeText(this,"판돈은 동일해야 합니다!",Toast.LENGTH_SHORT).show()

        }

        else if((p1b1!=p2b1||p1b2!=p2b2||p1b3!=p2b3||p1b4!=p2b4||p1b5!=p2b5) && (p1==1||p2==2)) {

            Toast.makeText(this,"판돈은 동일해야 합니다!",Toast.LENGTH_SHORT).show()

        }

        // 처음 누를때는(p1=0일때는) 주사위 1,2 만 바뀜(n1~n4만 바뀜)
        else if(p1==0 && p2 == 0) {

            p1_dice1.setBackgroundResource(R.drawable.background1)
            p1_dice2.setBackgroundResource(R.drawable.background1)

            n1 = (Math.random() * 6) + 1
            n2 = n1.toInt()

            n3 = (Math.random() * 6) + 1
            n4 = n3.toInt()

            p1_dice1.set_number(n2)
            p1_dice2.set_number(n4)

            Toast.makeText(this,""+name_2+" 의 차레입니다",Toast.LENGTH_LONG).show()


            p1 = p1 + 1

        }

        //두번쨰 누를때는(p1=1일때는) 주사위 3,4,5 만 바뀜(n5~n10만 바뀜)
        else if(p1==1 && p2 == 1) {

            p1_dice3.setBackgroundResource(R.drawable.background1)
            p1_dice4.setBackgroundResource(R.drawable.background1)
            p1_dice5.setBackgroundResource(R.drawable.background1)

            n1 = n1
            n2 = n1.toInt()

            n3 = n3
            n4 = n3.toInt()

            n5 = (Math.random() * 6) + 1
            n6 = n5.toInt()

            n7 = (Math.random() * 6) + 1
            n8 = n7.toInt()

            n9 = (Math.random() * 6) + 1
            n10 = n9.toInt()

            p1_dice1.set_number(n2)
            p1_dice2.set_number(n4)
            p1_dice3.set_number(n6)
            p1_dice4.set_number(n8)
            p1_dice5.set_number(n10)


            p1 = p1 + 1

            Toast.makeText(this,""+name_2+" 의 차레입니다",Toast.LENGTH_LONG).show()

            text1()


        }

        //세번쨰 누를때는(p1=2일때는) 선택된 주사위만 바뀜(button1~5가 노란색일떄만 바뀜)
        else if(p1==2 && p2 == 2) {

            if(s1_1 == 1) {
            n1 = (Math.random() * 6) + 1
            n2 = n1.toInt()}

            if(s1_2 == 1) {
            n3 = (Math.random() * 6) + 1
            n4 = n3.toInt()}

            if(s1_3 == 1) {
            n5 = (Math.random() * 6) + 1
            n6 = n5.toInt()}

            if(s1_4 == 1) {
            n7 = (Math.random() * 6) + 1
            n8 = n7.toInt()}

            if(s1_5 == 1) {
            n9 = (Math.random() * 6) + 1
            n10 = n9.toInt()}

            p1_dice1.set_number(n2)
            p1_dice2.set_number(n4)
            p1_dice3.set_number(n6)
            p1_dice4.set_number(n8)
            p1_dice5.set_number(n10)

            p1button1.setBackgroundResource(android.R.color.transparent)
            p1button2.setBackgroundResource(android.R.color.transparent)
            p1button3.setBackgroundResource(android.R.color.transparent)
            p1button4.setBackgroundResource(android.R.color.transparent)
            p1button5.setBackgroundResource(android.R.color.transparent)



            p1 = p1 + 1

            Toast.makeText(this,""+name_2+" 의 차레입니다",Toast.LENGTH_LONG).show()

            text1()


        }






    }

    //player2 의 주사위를 던질떄의 반응(아래 roll 버튼 눌렀을때)
    fun roll2(view: View) {

        if(p1b1+p1b2+p1b3+p1b4+p1b5+p2b1+p2b2+p2b3+p2b4+p2b5 == 0) {

            Toast.makeText(this,"참가비 먼저 걸어주세요!",Toast.LENGTH_SHORT).show()

        }

        else if((p1b1!=p2b1||p1b2!=p2b2||p1b3!=p2b3||p1b4!=p2b4||p1b5!=p2b5) && p1==0) {

            Toast.makeText(this,"판돈은 동일해야 합니다!",Toast.LENGTH_SHORT).show()

        }

        else if((p1b1!=p2b1||p1b2!=p2b2||p1b3!=p2b3||p1b4!=p2b4||p1b5!=p2b5) && (p1==1|| p1==2)) {

            Toast.makeText(this,"판돈은 동일해야 합니다!",Toast.LENGTH_SHORT).show()
        }

        else if (p1==p2 && p1==0) {

            Toast.makeText(this,"" + name_1 + "  의 차례입니다!",Toast.LENGTH_SHORT).show()

        }

        else if (p1==p2 && (p1==1 || p1==2)) {

            Toast.makeText(this,"" + name_1 + "  의 차례입니다!",Toast.LENGTH_SHORT).show()

        }



        else if(p1==1 && p2 == 0) {

            p2_dice1.setBackgroundResource(R.drawable.background1)
            p2_dice2.setBackgroundResource(R.drawable.background1)

            n11 = (Math.random() * 6) + 1
            n12 = n11.toInt()

            n13 = (Math.random() * 6) + 1
            n14 = n13.toInt()

            p2_dice1.set_number(n12)
            p2_dice2.set_number(n14)


            p2 = p2 + 1

            Toast.makeText(this,"첫번째 배팅 순서입니다!",Toast.LENGTH_LONG).show()

        }

        else if(p1== 2 && p2== 1) {

            p2_dice3.setBackgroundResource(R.drawable.background1)
            p2_dice4.setBackgroundResource(R.drawable.background1)
            p2_dice5.setBackgroundResource(R.drawable.background1)

            n11 = n11
            n12 = n11.toInt()

            n13 = n13
            n14 = n13.toInt()

            n15 = (Math.random() * 6) + 1
            n16 = n15.toInt()

            n17 = (Math.random() * 6) + 1
            n18 = n17.toInt()

            n19 = (Math.random() * 6) + 1
            n20 = n19.toInt()

            p2_dice1.set_number(n12)
            p2_dice2.set_number(n14)
            p2_dice3.set_number(n16)
            p2_dice4.set_number(n18)
            p2_dice5.set_number(n20)


            p2 = p2 + 1

            Toast.makeText(this,"역전의 기회!\n최종 배팅 후 바꾸고싶은 주사위를 선택해서 다시 던져보세요!\n* 주사위 0~5개 선택가능\n* 선택취소가능",Toast.LENGTH_LONG).show()

            text2()

        }

        else if(p1== 3 && p2== 2) {
            if(s2_1 == 1) {
            n11 = (Math.random() * 6) + 1
            n12 = n11.toInt()}

            if(s2_2 == 1) {
            n13 = (Math.random() * 6) + 1
            n14 = n13.toInt()}

            if(s2_3 == 1) {
            n15 = (Math.random() * 6) + 1
            n16 = n15.toInt()}

            if(s2_4 == 1) {
            n17 = (Math.random() * 6) + 1
            n18 = n17.toInt()}

            if(s2_5 == 1) {
            n19 = (Math.random() * 6) + 1
            n20 = n19.toInt()}

            p2_dice1.set_number(n12)
            p2_dice2.set_number(n14)
            p2_dice3.set_number(n16)
            p2_dice4.set_number(n18)
            p2_dice5.set_number(n20)

            p2button1.setBackgroundResource(android.R.color.transparent)
            p2button2.setBackgroundResource(android.R.color.transparent)
            p2button3.setBackgroundResource(android.R.color.transparent)
            p2button4.setBackgroundResource(android.R.color.transparent)
            p2button5.setBackgroundResource(android.R.color.transparent)

            p2 = p2 + 1

            Toast.makeText(this,"게임이 종료되었습니다",Toast.LENGTH_LONG).show()

            text2()

        }

    }


    // player1 의 주사위 패를 표시하는 함수
    fun text1() {

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

            p1_text.text = "One Pair"}

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
            p1_text.text = "Two Pair"}


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

            p1_text.text = "Triple"
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

            p1_text.text = "Full House"
        }


        else if((n2==n4&&n2==n6&&n2==n8&&n2!=n10) ||
                (n2==n4&&n2==n6&&n2==n8&&n2!=n10) ||
                (n2==n4&&n2==n6&&n2!=n8&&n2==n10) ||
                (n2==n4&&n2!=n6&&n2==n8&&n2==n10) ||
                (n2!=n4&&n2==n6&&n2==n8&&n2==n10) ||
                (n2!=n4&&n4==n6&&n4==n8&&n4==n10)) {

            p1_text.text = "Four Dice"
        }

        else if (n2==n4&&n2==n6&&n2==n8&&n2==n10) {

            p1_text.text = "King of Poker"
        }

        else if (n2!=n4&&n2!=n6&&n2!=n8&&n2!=n10&&n4!=n6&&n4!=n8&&n4!=n10&&n6!=n8&&n6!=n10&&n8!=n10&&n2+n4+n6+n8+n10==15) {

            p1_text.text = "Straight"
        }

        else if (n2!=n4&&n2!=n6&&n2!=n8&&n2!=n10&&n4!=n6&&n4!=n8&&n4!=n10&&n6!=n8&&n6!=n10&&n8!=n10&&n2+n4+n6+n8+n10==20) {

            p1_text.text = "Royal Straight"
        }


        else{ p1_text.text = "nothing" }

    }
    // player2 의 주사위 패를 표시하는 함수
    fun text2() {

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

            p2_text.text = "One Pair"}

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
            p2_text.text = "Two Pair"}


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

            p2_text.text = "Triple"
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

            p2_text.text = "Full House"
        }


        else if((n12==n14&&n12==n16&&n12==n18&&n12!=n20) ||
                (n12==n14&&n12==n16&&n12==n18&&n12!=n20) ||
                (n12==n14&&n12==n16&&n12!=n18&&n12==n20) ||
                (n12==n14&&n12!=n16&&n12==n18&&n12==n20) ||
                (n12!=n14&&n12==n16&&n12==n18&&n12==n20) ||
                (n12!=n14&&n14==n16&&n14==n18&&n14==n20)) {

            p2_text.text = "Four Dice"
        }

        else if (n12==n14&&n12==n16&&n12==n18&&n12==n20) {

            p2_text.text = "King of Poker"
        }

        else if (n12!=n14&&n12!=n16&&n12!=n18&&n12!=n20&&n14!=n16&&n14!=n18&&n14!=n20&&n16!=n18&&n16!=n20&&n18!=n20&&n12+n14+n16+n18+n20==15) {

            p2_text.text = "Straight"
        }

        else if (n12!=n14&&n12!=n16&&n12!=n18&&n12!=n20&&n14!=n16&&n14!=n18&&n14!=n20&&n16!=n18&&n16!=n20&&n18!=n20&&n12+n14+n16+n18+n20==20) {

            p2_text.text = "Royal Straight"
        }


        else{ p2_text.text = "nothing" }


    }

}
