package com.nyanyajaguar.qybg

import android.graphics.drawable.AnimationDrawable
import android.icu.util.ValueIterator
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    var n = 0 //0:roll, 1:classic, 2:quantum, 3:lock
    var m = 3 //0:roll, 1:classic, 2:quantum, 3:lock

    //윷말 결정
    var n1 :Double = 1.0
    var n2 = n1.toInt()
    //윷 던지는 시간
    var t1 : Double = 1.0
    var t2 = t1.toLong()
    //윷말텍스트
    var text1 : String = ""
    var text2 : String = ""
    var qt1_1 : String = ""
    var qt1_2 : String = ""
    //말종류
    var s1 = 0 // 0:lock, 1:도 2:개 3:걸 4:윷 5:모 6:뺵도
    var s2 = 0 //quantum으로 바꿀때는 text가 두개라서 변수 두개 쓰임

    var d1 = 0
    var d2 = 0 //quantum으로 바꿀때는 text가 두개라서 변수 두개 쓰임

    // 윷 결과 선택한 텍스트 번호
    var sel = 0

    //지금 선택된 말의 id
    var thisview : View ?= null

    //이동하고자 선택한 말의 id
    var selview : View ?= null
    //이동할 위치의 id(Int)
    var idnview = 0
    var idn_2view = 0
    var idn2view = 0

    //이동하고자 선택한 말의 번호
    var svn = 0
    var svn1 = 0 // quantum 말을 classic 으로 움직일때 선택한 말 외의 나머지 말의 번호
    var svn2 = 0 // quantum 말을 classic 으로 움직일때 선택한 말 외의 나머지 말의 번호(쌍이 여러개 있을떄)
    var idn = 0 //선택한 말이 갈 수 있는 위치에 대한 변수
    var idn2 = 0 //선택한 말이 갈림길에 있을 때 갈수있는 지름길에 대한 변수
    var idn_2 = 0 //선택한 말을 quantum 이동시킬때 갈 수 있는 지름길에 대한 변수

    //이동하고자 선택한 말의 background 이미지
    var selview_bgi : String = ""

    //내가 가려는 곳에 있는 다른 말의 id 와 번호
    var capture = 0
    var captureview = 0
    var capture2 = 0
    var capture2view = 0
    var capture3 = 0
    var capture3view = 0


    //내가 잡은 상대편 말의 background 이미지
    var capture_bgi : String = ""
    var capture_bgi2 : String = ""
    var capture_bgi3 : String = ""

    //승패가 났는지 확인하는 변수 0이면 승리
    var checknumber = 0

    //기본 진행순서
    //roll1 으로 firstroll1()진행 - 윷던지는 애니메이션 - 시간이 지나 끝나면 선택창띄우기
    //선택창에서 classic선택하면 classic진행 - 선택창 없애고 처음 나온 말이 윷이나 모면 아닌게 나올때까지 다시던짐
    //윷던지는게 끝나면 윷패가 적힌 텍스트선택하면 textclicklistener()진행 - 선택가능한 자기 말 표시
    //선택된 말 중에서 자기가 이동하고싶은 말 선택하면 imageBtnClickListener() 진행 - player1이 classic으로 이동할 때 이므로 classicmove1()진행
    //classicmove1() 중에서 선택한 말이 select일때 함수 진행 -


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(playername1 != ""){
            player1.text = playername1
        }
        else{
            playername1 = "이름 없음 1"
            player1.text = playername1
        }

        if(playername2 != ""){
            player2.text = playername2
        }
        else{
            playername2 = "이름 없음 2"
            player2.text = playername2
        }


        if(board.height>= board.width){


            yut_board.layoutParams.width = board.width
            yut_board.layoutParams.height = yut_board.width
            yut_board.requestLayout()

        }
        else if(board.height < board.width){

            yut_board.layoutParams.height = board.height
            yut_board.layoutParams.width = yut_board.height
            yut_board.requestLayout()

        }

        //어떤 위치 선택되면 해당 위치/말에 대해 이미지 바꾼다 imageBtnClickListener 이용해서
        for (i in 0..36) {
            val btnId = resources.getIdentifier("imageButton$i", "id", packageName)
            findViewById<View>(btnId).setOnClickListener(imageBtnClickListener)
        }

        for (j in 1..4){
            val textId = resources.getIdentifier("t1_$j", "id", packageName)
            findViewById<TextView>(textId).setOnClickListener(textclicklistener)
        }

        for (k in 1..4){
            val textId = resources.getIdentifier("t2_$k", "id", packageName)
            findViewById<TextView>(textId).setOnClickListener(textclicklistener2)
        }

    }

    //윷 말패 텍스트 선택했을떄 해당 텍스트에 있는 윷패만큼 이동할 말 선택창 표시
    private val textclicklistener = View.OnClickListener {
        if(notice.getVisibility() == View.GONE && n == 1){
            it.apply {

                val selnumber = it.context.resources.getResourceEntryName(it.id).substring(3).toInt()

                if((it as TextView).text != "" && sel == 0){
                    sel = selnumber
                    it.setBackgroundResource(R.color.bgi_orange)
                    text1 = (it as TextView).text.toString()
                    if (text1 == "도") {
                        s1 = 1
                    } else if (text1 == "개") {
                        s1 = 2
                    } else if (text1 == "걸") {
                        s1 = 3
                    } else if (text1 == "윷") {
                        s1 = 4
                    } else if (text1 == "모") {
                        s1 = 5
                    } else if (text1 == "빽도") {
                        s1 = 6
                    }
                    showselect3()

                }
                else if(sel == selnumber){
                    sel = 0
                    it.setBackgroundResource(android.R.color.transparent)
                    delsel()
                }

            }
        }
    }
    private val textclicklistener2 = View.OnClickListener {
        if(notice.getVisibility() == View.GONE && m ==1){
            it.apply {

                val selnumber = it.context.resources.getResourceEntryName(it.id).substring(3).toInt()

                if((it as TextView).text != "" && sel == 0){
                    sel = selnumber
                    it.setBackgroundResource(R.color.bgi_orange)
                    text2 = (it as TextView).text.toString()
                    if (text2 == "도") {
                        d1 = 1
                    } else if (text2 == "개") {
                        d1 = 2
                    } else if (text2 == "걸") {
                        d1 = 3
                    } else if (text2 == "윷") {
                        d1 = 4
                    } else if (text2 == "모") {
                        d1 = 5
                    } else if (text2 == "빽도") {
                        d1 = 6
                    }
                    showselect4()

                }
                else if(sel == selnumber){
                    sel = 0
                    it.setBackgroundResource(android.R.color.transparent)
                    delsel()
                }

            }
        }
    }

    //말 선택햇을때 이미지 변경
    private val imageBtnClickListener = View.OnClickListener {
        if(notice.getVisibility() == View.GONE){
            (it as CustomImageButton).apply {

                thisview = it

                //player1이 classic 말 이동 선택하는 경우
                if (n == 1 && m == 3) {

                    classicmove1()

                }

                //player1이 quantum 말 이동 선택하는 경우
                else if(n==2 && m == 3){

                    quantummove1()
                }

                //player2가 classic 말 이동 선택하는 경우
                else if(m==1 && n == 3){

                    classicmove2()

                }

                //player2가 quantum 말 이동 선택하는 경우
                else if(m==2 && n == 3){

                    quantummove2()
                }

            }
        }

    }

    //player1이 말을 classic으로 이동 선택하는 경우
    fun classicmove1(){
        //thisview 는 방금 선택한 버튼
        (thisview as CustomImageButton).apply{

            //이동시키고싶은 말 선택
            if (sameCheck2(R.drawable.select)) {
                //selview 는 이동하기위해 선택한 버튼, svn은 이동하기위해 선택한 버튼 번호
                if(svn == 0){
                    selview = thisview
                    svn = (selview as ImageButton)!!.context.resources.getResourceEntryName((selview as ImageButton)!!.id).substring(11).toInt()
                    if (sameCheck(R.drawable.cb1)) {
                        selview_bgi = "cb1"
                    } else if (sameCheck(R.drawable.cb2)) {
                        selview_bgi = "cb2"
                    } else if (sameCheck(R.drawable.cb12)) {
                        selview_bgi = "cb12"
                    } else if (sameCheck(R.drawable.cb1qb2y)) {
                        selview_bgi = "cb1qb2y"
                    } else if (sameCheck(R.drawable.cb1qb2p)) {
                        selview_bgi = "cb1qb2p"
                    } else if (sameCheck(R.drawable.cb2qb1y)) {
                        selview_bgi = "cb2qb1y"
                    } else if (sameCheck(R.drawable.cb2qb1p)) {
                        selview_bgi = "cb2qb1p"
                    } else if (sameCheck(R.drawable.qb1)) {
                        selview_bgi = "qb1"
                    } else if (sameCheck(R.drawable.qb1y)) {
                        selview_bgi = "qb1y"
                    } else if (sameCheck(R.drawable.qb1p)) {
                        selview_bgi = "qb1p"
                    } else if (sameCheck(R.drawable.qb2)) {
                        selview_bgi = "qb2"
                    } else if (sameCheck(R.drawable.qb2y)) {
                        selview_bgi = "qb2y"
                    } else if (sameCheck(R.drawable.qb2p)) {
                        selview_bgi = "qb2p"
                    } else if (sameCheck(R.drawable.qb12y)) {
                        selview_bgi = "qb12y"
                    } else if (sameCheck(R.drawable.qb12p)) {
                        selview_bgi = "qb12p"
                    }

                    showselect5()
                }
                else if(svn != 0){

                    delsel()
                    showselect3()
                    svn = 0
                    svn1 = 0
                    idn = 0
                    idn_2 = 0

                }

            }

            //해당 말이 이동할 위치 선택
            else if(sameCheck2(R.drawable.select2)){

                //quantum화된 애들을 옮기는 경우
                if(selview_bgi.substring(0,1) == "q"
                        || selview_bgi.substring(selview_bgi.length -1) == "p" || selview_bgi.substring(selview_bgi.length -1) == "y"){

                    subcm1()

                }
                //classic 말을 classic으로 옮기는 경우
                else {
                    //내가 가고자하는 곳에 이미 상대편 classic 말이 있으면 잡고 한번 더 돌림
                    if(sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2)||sameCheck(R.drawable.cr12)){
                        //잡은 말 정리
                        if(sameCheck(R.drawable.cr1)){
                            capture_bgi = "cr1"
                            imageButton33.setBackgroundResource(R.drawable.cr1)
                        }
                        else if(sameCheck(R.drawable.cr2)){
                            capture_bgi = "cr2"
                            imageButton34.setBackgroundResource(R.drawable.cr2)
                        }
                        else if(sameCheck(R.drawable.cr12)){
                            capture_bgi = "cr12"
                            imageButton33.setBackgroundResource(R.drawable.cr1)
                            imageButton34.setBackgroundResource(R.drawable.cr2)
                        }

                        //선택표시창 지우고, 원래 말 지우고, 선택관련 변수 초기화
                        final()
                        //새 위치에 말 이동하기
                        if(selview_bgi == "cb1"){
                            setBackgroundResource(R.drawable.cb1)
                        }
                        else if(selview_bgi == "cb2"){
                            setBackgroundResource(R.drawable.cb2)
                        }
                        else if(selview_bgi == "cb12"){
                            setBackgroundResource(R.drawable.cb12)
                        }

                        //잡았으니까 다시 돌리고 윷/모 나오면 한번 더 진행
                        firstroll1()
                        val handler = Handler()
                        handler.postDelayed({
                            if(text1 == "윷" || text1 == "모"){
                                firstroll1()
                                val handler = Handler()
                                handler.postDelayed({
                                    if(text1 == "윷" || text1 == "모"){
                                        firstroll1()
                                    }
                                },3000)
                            }
                        },1500)
                    }

                    //선택한 곳에 내 classic 말이 있으면 업기
                    else if(sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2)){
                        //원래 있던 말 정리
                        if(sameCheck(R.drawable.cb1)){
                            capture_bgi = "cb1"
                        }
                        else if(sameCheck(R.drawable.cb2)){
                            capture_bgi = "cb2"
                        }


                        final()
                        //새 위치에 말 이동하기
                        if((selview_bgi == "cb1" && capture_bgi == "cb2")||(selview_bgi =="cb2" && capture_bgi == "cb1")){
                            setBackgroundResource(R.drawable.cb12)
                        }

                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                            check()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                s1=0
                            }
                            else{
                                n=3
                                m=0
                                s1=0
                                s2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                selview_bgi = ""
                                capture_bgi = ""

                                checknumber = 0
                                t1_4.text = ""
                            }
                        }
                    }

                    //선택한 곳이 비어있는곳이면 그냥 옮기기
                    else if(sameCheck(R.drawable.base)){
                        //선택표시창 지우고, 원래 말 지우고, 선택관련 변수 초기화
                        final()
                        //새 위치에 classic 말 이동하기
                        if(selview_bgi == "cb1"){
                            setBackgroundResource(R.drawable.cb1)
                        } else if(selview_bgi == "cb2"){
                            setBackgroundResource(R.drawable.cb2)
                        } else if(selview_bgi == "cb12"){
                            setBackgroundResource(R.drawable.cb12)
                        }

                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                            check()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                s1=0
                            }
                            else{
                                n=3
                                m=0
                                s1=0
                                s2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                selview_bgi = ""
                                capture_bgi = ""

                                checknumber = 0
                                t1_4.text = ""
                            }
                        }
                    }

                    //선택한 곳에 내 quantum 말이 있는 경우
                    else if(sameCheck(R.drawable.qb1)||sameCheck(R.drawable.qb2)){

                        if(sameCheck(R.drawable.qb1) && selview_bgi == "cb2"){

                            capture_bgi = "qb1"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.qb2) && selview_bgi == "cb1"){

                            capture_bgi = "qb2"
                            subcm1()

                        }

                    }

                    //선택한 곳에 상대방 quantum 말이 있는 경우
                    else if(sameCheck(R.drawable.qr1)||sameCheck(R.drawable.qr1y)||sameCheck(R.drawable.qr1p)
                            ||sameCheck(R.drawable.qr2)||sameCheck(R.drawable.qr2y)||sameCheck(R.drawable.qr2p)
                            ||sameCheck(R.drawable.qr12y)||sameCheck(R.drawable.qr12p)
                            ||sameCheck(R.drawable.cr1qr2y)||sameCheck(R.drawable.cr1qr2p)
                            ||sameCheck(R.drawable.cr2qr1y)||sameCheck(R.drawable.cr2qr1p)){

                        if(sameCheck(R.drawable.qr1)){

                            capture_bgi = "qr1"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.qr1y)){

                            capture_bgi = "qr1y"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.qr1p)){

                            capture_bgi = "qr1p"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.qr2)){

                            capture_bgi = "qr2"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.qr2p)){

                            capture_bgi = "qr2p"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.qr2y)){

                            capture_bgi = "qr2y"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.qr12y)){

                            capture_bgi = "qr12y"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.qr12p)){

                            capture_bgi = "qr12p"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.cr1qr2y)){

                            capture_bgi = "cr1qr2y"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.cr1qr2p)){

                            capture_bgi = "cr1qr2p"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.cr2qr1y)){

                            capture_bgi = "cr2qr1y"
                            subcm1()

                        }
                        else if(sameCheck(R.drawable.cr2qr1p)){

                            capture_bgi = "cr2qr1p"
                            subcm1()

                        }

                    }

                }



            }

        }
    }

    //quantum 말을 놓을때
    fun subcm1(){

        final()

        //classic 말을 classic으로 옮길 위치에 quantum이 있을 때
        if(n==1 && selview_bgi.substring(0,1) == "c"){

            //classic이 이동하려고 선택한 곳에 내 quantum 말이 있는 경우
            if(capture_bgi == "qb1" || capture_bgi == "qb2"){

                capture = (thisview as ImageButton)!!.context.resources.getResourceEntryName((thisview as ImageButton)!!.id).substring(11).toInt()

                if(capture_bgi == "qb1"){
                    for(i in 0..28){
                        if(i != capture) { //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                            val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                            if ((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                capture2 = i

                            }
                        }
                    }

                    captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                    capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2qb1y)
                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1p)

                }
                else if(capture_bgi == "qb2"){
                    for(i in 0..28){
                        if(i != capture) { //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                            val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                            if ((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                capture2 = i

                            }
                        }
                    }

                    captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                    capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1qb2y)
                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2p)

                }

                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                    check()
                    if(checknumber == 0){
                        Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                        n=3
                        m=3
                        s1=0
                    }
                    else{
                        n=3
                        m=0
                        s1=0
                        s2 = 0
                        idn = 0
                        idn_2 = 0
                        idn2 = 0
                        sel = 0
                        svn = 0
                        svn1 = 0
                        svn2 = 0
                        capture = 0
                        captureview = 0
                        capture2 = 0
                        capture2view = 0
                        selview_bgi = ""
                        capture_bgi = ""

                        checknumber = 0
                        t1_4.text = ""
                    }
                }

            }

            //classic이 이동하려고 선택한 곳에 상대방 quantum 말이 있는 경우
            else if(capture_bgi == "qr1"||capture_bgi == "qr1y"||capture_bgi == "qr1p"
                    ||capture_bgi == "qr2"||capture_bgi == "qr2y"||capture_bgi == "qr2p"
                    ||capture_bgi == "qr12y"||capture_bgi == "qr12p"
                    ||capture_bgi == "cr1qr2y"||capture_bgi == "cr1qr2p"
                    ||capture_bgi == "cr2qr1y"||capture_bgi == "cr2qr1p"){

                //선택한 곳 번호 확인(상대방 quantum말이 있는 곳)
                capture = (thisview as ImageButton)!!.context.resources.getResourceEntryName((thisview as ImageButton)!!.id).substring(11).toInt()

                if(selview_bgi == "cb1"){

                    if(capture_bgi == "qr1"){
                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },2000)


                    }
                    else if(capture_bgi == "qr1p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 !=0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)
                            findViewById<View>(capture3view).setBackgroundResource(R.drawable.qr2p)


                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr2)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)



                        }
                        //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                        else if(capture2 == 0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2qr1y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qr2"){
                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },2000)


                    }
                    else if(capture_bgi == "qr2p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 !=0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)
                            findViewById<View>(capture3view).setBackgroundResource(R.drawable.qr1p)


                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)



                        }
                        //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                        else if(capture2 == 0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1qr2y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qr12y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr12y가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 ==0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }


                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr2)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)



                        }
                        //잡은 qr12y가 qr12y, qr12p 쌍이면
                        else if(capture2 != 0){

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12p)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qr12p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        //잡은 qr12p가 qr12y, qr12p 쌍이면
                        if(capture2 != 0){

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "cr1qr2y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2p)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },2000)

                    }
                    else if(capture_bgi == "cr2qr1y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1p)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },2000)

                    }

                }
                else if(selview_bgi == "cb2"){

                    if(capture_bgi == "qr1"){
                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },2000)


                    }
                    else if(capture_bgi == "qr1p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 !=0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)
                            findViewById<View>(capture3view).setBackgroundResource(R.drawable.qr2p)


                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr2)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)



                        }
                        //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                        else if(capture2 == 0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2qr1y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qr2"){
                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },2000)


                    }
                    else if(capture_bgi == "qr2p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 !=0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)
                            findViewById<View>(capture3view).setBackgroundResource(R.drawable.qr1p)


                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)



                        }
                        //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                        else if(capture2 == 0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1qr2y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qr12y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr12y가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 ==0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }


                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr2)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)



                        }
                        //잡은 qr12y가 qr12y, qr12p 쌍이면
                        else if(capture2 != 0){

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12p)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qr12p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        //잡은 qr12p가 qr12y, qr12p 쌍이면
                        if(capture2 != 0){

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "cr1qr2y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2p)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },2000)

                    }
                    else if(capture_bgi == "cr2qr1y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1p)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },2000)

                    }

                }

            }

            //classic에 quantum 업힌게 이동하는 경우
            else if(selview_bgi.substring(selview_bgi.length -1) == "p" || selview_bgi.substring(selview_bgi.length -1) == "y"){

                if(selview_bgi == "cb1qb2y"){
                    //svn : cb1qb2y 위치, idn : cb1qb2y 가 이동할 위치, svn1 : qb2p 위치, idn_2 : qb2p가 이동할 위치
                    //이동할 두 곳에 뭐가 있는지 확인
                    (findViewById<View>(idnview) as CustomImageButton).apply{
                        //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                        if(sameCheck(R.drawable.base)){
                            capture_bgi = "base"
                        }
                        else if(sameCheck(R.drawable.cr1)){
                            capture_bgi = "cr1"
                        }
                        else if(sameCheck(R.drawable.cr2)){
                            capture_bgi = "cr2"
                        }
                        else if(sameCheck(R.drawable.cr12)){
                            capture_bgi = "cr12"
                        }
                        else if(sameCheck(R.drawable.qr1)){
                            capture_bgi = "qr1"
                        }
                        else if(sameCheck(R.drawable.qr1p)){
                            capture_bgi = "qr1p"
                        }
                        else if(sameCheck(R.drawable.qr2)){
                            capture_bgi = "qr2"
                        }
                        else if(sameCheck(R.drawable.qr2p)){
                            capture_bgi = "qr2p"
                        }
                        else if(sameCheck(R.drawable.qr12y)){
                            capture_bgi = "qr12y"
                        }
                        else if(sameCheck(R.drawable.qr12p)){
                            capture_bgi = "qr12p"
                        }
                        else if(sameCheck(R.drawable.cr1qr2y)){
                            capture_bgi = "cr1qr2y"
                        }
                        else if(sameCheck(R.drawable.cr2qr1y)){
                            capture_bgi = "cr2qr1y"
                        }
                    }
                    (findViewById<View>(idn_2view) as CustomImageButton).apply{
                        //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                        if(sameCheck(R.drawable.base)){
                            capture_bgi2 = "base"
                        }
                        else if(sameCheck(R.drawable.cr1)){
                            capture_bgi2 = "cr1"
                        }
                        else if(sameCheck(R.drawable.cr2)){
                            capture_bgi2 = "cr2"
                        }
                        else if(sameCheck(R.drawable.cr12)){
                            capture_bgi2 = "cr12"
                        }
                        else if(sameCheck(R.drawable.qr1)){
                            capture_bgi2 = "qr1"
                        }
                        else if(sameCheck(R.drawable.qr1p)){
                            capture_bgi2 = "qr1p"
                        }
                        else if(sameCheck(R.drawable.qr2)){
                            capture_bgi2 = "qr2"
                        }
                        else if(sameCheck(R.drawable.qr2p)){
                            capture_bgi2 = "qr2p"
                        }
                        else if(sameCheck(R.drawable.qr12y)){
                            capture_bgi2 = "qr12y"
                        }
                        else if(sameCheck(R.drawable.qr12p)){
                            capture_bgi2 = "qr12p"
                        }
                        else if(sameCheck(R.drawable.cr1qr2y)){
                            capture_bgi2 = "cr1qr2y"
                        }
                        else if(sameCheck(R.drawable.cr2qr1y)){
                            capture_bgi2 = "cr2qr1y"
                        }
                    }

                    if(capture_bgi == "base" && capture_bgi2 == "base"){
                        final()
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                            check()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                s1=0
                            }
                            else{
                                n=3
                                m=0
                                s1=0
                                s2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""

                                checknumber = 0
                                t1_4.text = ""
                            }
                        }


                    }

                    else if(capture_bgi == "base" && capture_bgi2 != "base" ){
                        //선택한 곳 번호 확인(상대방 quantum말이 있는 곳)
                        capture = (thisview as ImageButton)!!.context.resources.getResourceEntryName((thisview as ImageButton)!!.id).substring(11).toInt()

                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1qb2y)

                        if(capture_bgi2 == "qr1"){
                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)


                        }
                        else if(capture_bgi2 == "qr1p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 !=0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)
                                findViewById<View>(capture3view).setBackgroundResource(R.drawable.qr2p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr2)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)



                            }
                            //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                            else if(capture2 == 0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2qr1y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qr2"){
                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)


                        }
                        else if(capture_bgi2 == "qr2p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 !=0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)
                                findViewById<View>(capture3view).setBackgroundResource(R.drawable.qr1p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)



                            }
                            //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                            else if(capture2 == 0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1qr2y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qr12y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr12y가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 ==0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }


                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr2)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)



                            }
                            //잡은 qr12y가 qr12y, qr12p 쌍이면
                            else if(capture2 != 0){

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qr12p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            //잡은 qr12p가 qr12y, qr12p 쌍이면
                            if(capture2 != 0){

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "cr1qr2y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2p)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }
                        else if(capture_bgi2 == "cr2qr1y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qb2p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1p)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }


                }

                else if(selview_bgi == "cb2qb1y"){
                    //svn : cb1qb2y 위치, idn : cb1qb2y 가 이동할 위치, svn1 : qb2p 위치, idn_2 : qb2p가 이동할 위치
                    //이동할 두 곳에 뭐가 있는지 확인
                    (findViewById<View>(idnview) as CustomImageButton).apply{
                        //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                        if(sameCheck(R.drawable.base)){
                            capture_bgi = "base"
                        }
                        else if(sameCheck(R.drawable.cr1)){
                            capture_bgi = "cr1"
                        }
                        else if(sameCheck(R.drawable.cr2)){
                            capture_bgi = "cr2"
                        }
                        else if(sameCheck(R.drawable.cr12)){
                            capture_bgi = "cr12"
                        }
                        else if(sameCheck(R.drawable.qr1)){
                            capture_bgi = "qr1"
                        }
                        else if(sameCheck(R.drawable.qr1p)){
                            capture_bgi = "qr1p"
                        }
                        else if(sameCheck(R.drawable.qr2)){
                            capture_bgi = "qr2"
                        }
                        else if(sameCheck(R.drawable.qr2p)){
                            capture_bgi = "qr2p"
                        }
                        else if(sameCheck(R.drawable.qr12y)){
                            capture_bgi = "qr12y"
                        }
                        else if(sameCheck(R.drawable.qr12p)){
                            capture_bgi = "qr12p"
                        }
                        else if(sameCheck(R.drawable.cr1qr2y)){
                            capture_bgi = "cr1qr2y"
                        }
                        else if(sameCheck(R.drawable.cr2qr1y)){
                            capture_bgi = "cr2qr1y"
                        }
                    }
                    (findViewById<View>(idn_2view) as CustomImageButton).apply{
                        //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                        if(sameCheck(R.drawable.base)){
                            capture_bgi2 = "base"
                        }
                        else if(sameCheck(R.drawable.cr1)){
                            capture_bgi2 = "cr1"
                        }
                        else if(sameCheck(R.drawable.cr2)){
                            capture_bgi2 = "cr2"
                        }
                        else if(sameCheck(R.drawable.cr12)){
                            capture_bgi2 = "cr12"
                        }
                        else if(sameCheck(R.drawable.qr1)){
                            capture_bgi2 = "qr1"
                        }
                        else if(sameCheck(R.drawable.qr1p)){
                            capture_bgi2 = "qr1p"
                        }
                        else if(sameCheck(R.drawable.qr2)){
                            capture_bgi2 = "qr2"
                        }
                        else if(sameCheck(R.drawable.qr2p)){
                            capture_bgi2 = "qr2p"
                        }
                        else if(sameCheck(R.drawable.qr12y)){
                            capture_bgi2 = "qr12y"
                        }
                        else if(sameCheck(R.drawable.qr12p)){
                            capture_bgi2 = "qr12p"
                        }
                        else if(sameCheck(R.drawable.cr1qr2y)){
                            capture_bgi2 = "cr1qr2y"
                        }
                        else if(sameCheck(R.drawable.cr2qr1y)){
                            capture_bgi2 = "cr2qr1y"
                        }
                    }

                    if(capture_bgi == "base" && capture_bgi2 == "base"){
                        final()
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                            check()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                s1=0
                            }
                            else{
                                n=3
                                m=0
                                s1=0
                                s2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""

                                checknumber = 0
                                t1_4.text = ""
                            }
                        }


                    }

                    else if(capture_bgi == "base" && capture_bgi2 != "base" ){
                        //선택한 곳 번호 확인(상대방 quantum말이 있는 곳)
                        capture = (thisview as ImageButton)!!.context.resources.getResourceEntryName((thisview as ImageButton)!!.id).substring(11).toInt()

                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2qb1y)

                        //(181103 03:08 수정작업중)
                        if(capture_bgi2 == "qr1"){
                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)


                        }
                        else if(capture_bgi2 == "qr1p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 !=0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)
                                findViewById<View>(capture3view).setBackgroundResource(R.drawable.qr2p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr2)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)



                            }
                            //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                            else if(capture2 == 0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2qr1y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qr2"){
                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)


                        }
                        else if(capture_bgi2 == "qr2p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 !=0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)
                                findViewById<View>(capture3view).setBackgroundResource(R.drawable.qr1p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)



                            }
                            //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                            else if(capture2 == 0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1qr2y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qr12y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr12y가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 ==0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }


                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cr2)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)



                            }
                            //잡은 qr12y가 qr12y, qr12p 쌍이면
                            else if(capture2 != 0){

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qr12p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            //잡은 qr12p가 qr12y, qr12p 쌍이면
                            if(capture2 != 0){

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr12y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "cr1qr2y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2p)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr2)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }
                        else if(capture_bgi2 == "cr2qr1y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qb1p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1p)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cr1)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton33.setBackgroundResource(R.drawable.cr1)
                                    imageButton34.setBackgroundResource(R.drawable.cr2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll1()
                            },2000)

                        }

                    }


                }


            }

        }

        //quantum 말을 classic으로 옮기거나 classic 말을 quantum으로 옮길때, quantum 말이 어떻게 놓여지는지에 대한 코드
        else if(n==2 || selview_bgi.substring(0,1) == "q"){

            //이동할 두 곳에 뭐가 있는지 확인
            (findViewById<View>(idnview) as CustomImageButton).apply{
                //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                if(sameCheck(R.drawable.base)){capture_bgi = "빈칸"}
                else if(sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2)||sameCheck(R.drawable.cb12)
                        ||sameCheck(R.drawable.qb1)||sameCheck(R.drawable.qb2)
                        ||sameCheck(R.drawable.qb1y)||sameCheck(R.drawable.qb2y)||sameCheck(R.drawable.qb12y)
                        ||sameCheck(R.drawable.qb1p)||sameCheck(R.drawable.qb2p)||sameCheck(R.drawable.qb12p)
                        ||sameCheck(R.drawable.cb1qb2y)||sameCheck(R.drawable.cb2qb1y)){capture_bgi = "내말"}
                else if(sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2)||sameCheck(R.drawable.cr12)
                        ||sameCheck(R.drawable.qr1)||sameCheck(R.drawable.qr2)
                        ||sameCheck(R.drawable.qr1y)||sameCheck(R.drawable.qr2y)||sameCheck(R.drawable.qr12y)
                        ||sameCheck(R.drawable.qr1p)||sameCheck(R.drawable.qr2p)||sameCheck(R.drawable.qr12p)
                        ||sameCheck(R.drawable.cr1qr2y)||sameCheck(R.drawable.cr2qr1y)){capture_bgi ="상대편말"}
            }
            (findViewById<View>(idn_2view) as CustomImageButton).apply{
                //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                if(sameCheck(R.drawable.base)){capture_bgi2 = "빈칸"}
                else if(sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2)||sameCheck(R.drawable.cb12)
                        ||sameCheck(R.drawable.qb1)||sameCheck(R.drawable.qb2)
                        ||sameCheck(R.drawable.qb1y)||sameCheck(R.drawable.qb2y) ||sameCheck(R.drawable.qb12y)
                        ||sameCheck(R.drawable.qb1p)||sameCheck(R.drawable.qb2p) ||sameCheck(R.drawable.qb12p)
                        ||sameCheck(R.drawable.cb1qb2y)||sameCheck(R.drawable.cb2qb1y)){capture_bgi2 = "내말"}
                else if(sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2) ||sameCheck(R.drawable.cr12)
                        ||sameCheck(R.drawable.qr1)||sameCheck(R.drawable.qr2)
                        ||sameCheck(R.drawable.qr1y)||sameCheck(R.drawable.qr2y) ||sameCheck(R.drawable.qr12y)
                        ||sameCheck(R.drawable.qr1p)||sameCheck(R.drawable.qr2p) ||sameCheck(R.drawable.qr12p)
                        ||sameCheck(R.drawable.cr1qr2y)||sameCheck(R.drawable.cr2qr1y)){capture_bgi2 ="상대편말"}
            }


            //이동하고자 하는 위치 두 곳 다 비어있을때
            if(capture_bgi == "빈칸" && capture_bgi2 == "빈칸"){

                if(selview_bgi == "qb1" || selview_bgi == "cb1"){

                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1)

                    if(idn == 0 || idn_2 == 0){

                        if(idn == 0 && idn_2 == 0){
                            findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)

                            //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                            if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                                check()
                                t1_4.text = "" + checknumber
                                if(checknumber == 0){
                                    Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                    n=3
                                    m=3
                                    s1=0
                                    selview_bgi = ""
                                }
                                else{
                                    n=3
                                    m=0
                                    s1=0
                                    selview_bgi = ""
                                    checknumber = 0
                                    t1_4.text = ""
                                    if(n==2) {
                                        text1 = ""
                                        qt1_1 = ""
                                        qt1_2 = ""
                                        s2 = 0
                                        svn = 0
                                        idn = 0
                                        idn_2 = 0
                                    }
                                }
                            }

                        } else{ // 하나만 홈에 도착하면 확률선택으로 결정됨

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()


                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                }

                                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                                if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                                    check()
                                    t1_4.text = "" + checknumber
                                    if(checknumber == 0){
                                        Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                        n=3
                                        m=3
                                        s1=0
                                        selview_bgi = ""
                                    }
                                    else{
                                        n=3
                                        m=0
                                        s1=0
                                        selview_bgi = ""
                                        checknumber = 0
                                        t1_4.text = ""
                                        if(n==2) {
                                            text1 = ""
                                            qt1_1 = ""
                                            qt1_2 = ""
                                            s2 = 0
                                            svn = 0
                                            idn = 0
                                            idn_2 = 0
                                        }
                                    }
                                }

                            },1500)
                        }
                    }
                    else{
                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                            check()
                            t1_4.text = "" + checknumber
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                s1=0
                                selview_bgi = ""
                            }
                            else{
                                n=3
                                m=0
                                s1=0
                                selview_bgi = ""
                                checknumber = 0
                                t1_4.text = ""
                                if(n==2) {
                                    text1 = ""
                                    qt1_1 = ""
                                    qt1_2 = ""
                                    s2 = 0
                                    svn = 0
                                    idn = 0
                                    idn_2 = 0
                                }
                            }
                        }
                    }

                }
                else if(selview_bgi == "qb2" || selview_bgi == "cb2"){
                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2)

                    if(idn == 0 || idn_2 == 0){

                        if(idn == 0 && idn_2 == 0){
                            findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)

                            //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                            if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                                check()
                                t1_4.text = "" + checknumber
                                if(checknumber == 0){
                                    Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                    n=3
                                    m=3
                                    s1=0
                                    selview_bgi = ""
                                }
                                else{
                                    n=3
                                    m=0
                                    s1=0
                                    selview_bgi = ""
                                    checknumber = 0
                                    t1_4.text = ""
                                    if(n==2) {
                                        text1 = ""
                                        qt1_1 = ""
                                        qt1_2 = ""
                                        s2 = 0
                                        svn = 0
                                        idn = 0
                                        idn_2 = 0
                                    }
                                }
                            }

                        } else{ // 하나만 홈에 도착하면 확률선택으로 결정됨

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()


                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                }

                                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                                if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                                    check()
                                    t1_4.text = "" + checknumber
                                    if(checknumber == 0){
                                        Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                        n=3
                                        m=3
                                        s1=0
                                        selview_bgi = ""
                                    }
                                    else{
                                        n=3
                                        m=0
                                        s1=0
                                        selview_bgi = ""
                                        checknumber = 0
                                        t1_4.text = ""
                                        if(n==2) {
                                            text1 = ""
                                            qt1_1 = ""
                                            qt1_2 = ""
                                            s2 = 0
                                            svn = 0
                                            idn = 0
                                            idn_2 = 0
                                        }
                                    }
                                }

                            },1500)
                        }
                    }
                    else{
                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                            check()
                            t1_4.text = "" + checknumber
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                s1=0
                                selview_bgi = ""
                            }
                            else{
                                n=3
                                m=0
                                s1=0
                                selview_bgi = ""
                                checknumber = 0
                                t1_4.text = ""
                                if(n==2) {
                                    text1 = ""
                                    qt1_1 = ""
                                    qt1_2 = ""
                                    s2 = 0
                                    svn = 0
                                    idn = 0
                                    idn_2 = 0
                                }
                            }
                        }
                    }

                }
                else if(selview_bgi == "qb12y" || selview_bgi == "qb12p" || selview_bgi == "cb12"){
                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb12y)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb12p)

                    if(idn == 0 || idn_2 == 0){

                        if(idn == 0 && idn_2 == 0){
                            findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)

                            //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                            if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                                check()
                                t1_4.text = "" + checknumber
                                if(checknumber == 0){
                                    Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                    n=3
                                    m=3
                                    s1=0
                                    selview_bgi = ""
                                }
                                else{
                                    n=3
                                    m=0
                                    s1=0
                                    selview_bgi = ""
                                    checknumber = 0
                                    t1_4.text = ""
                                    if(n==2) {
                                        text1 = ""
                                        qt1_1 = ""
                                        qt1_2 = ""
                                        s2 = 0
                                        svn = 0
                                        idn = 0
                                        idn_2 = 0
                                    }
                                }
                            }

                        } else{ // 하나만 홈에 도착하면 확률선택으로 결정됨

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()


                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                }

                                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                                if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                                    check()
                                    t1_4.text = "" + checknumber
                                    if(checknumber == 0){
                                        Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                        n=3
                                        m=3
                                        s1=0
                                        selview_bgi = ""
                                    }
                                    else{
                                        n=3
                                        m=0
                                        s1=0
                                        selview_bgi = ""
                                        checknumber = 0
                                        t1_4.text = ""
                                        if(n==2) {
                                            text1 = ""
                                            qt1_1 = ""
                                            qt1_2 = ""
                                            s2 = 0
                                            svn = 0
                                            idn = 0
                                            idn_2 = 0
                                        }
                                    }
                                }

                            },1500)
                        }
                    }
                    else{
                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                            check()
                            t1_4.text = "" + checknumber
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                s1=0
                                selview_bgi = ""
                            }
                            else{
                                n=3
                                m=0
                                s1=0
                                selview_bgi = ""
                                checknumber = 0
                                t1_4.text = ""
                                if(n==2) {
                                    text1 = ""
                                    qt1_1 = ""
                                    qt1_2 = ""
                                    s2 = 0
                                    svn = 0
                                    idn = 0
                                    idn_2 = 0
                                }
                            }
                        }
                    }

                }

            }

            // idnview 나 idn_2view에 내 다른 말이 있어서 업을 때
            else if(capture_bgi == "내말" || capture_bgi2 == "내말"){
                final()
                //옮기는 말이 qb2거나 cb2의 quantum패 일때
                if(selview_bgi == "qb2" || selview_bgi == "cb2"){
                    //이동할 위치중에 cb1 이 있으면
                    if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.base)){
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)
                    }
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1)
                            && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.base)){
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1qb2y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p)
                    }
                    //이동할 위치중에 qb1이 있으면
                    //둘다 qb1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb12y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb12p)
                    }
                    //선택한데만 qb1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    idn_2 = i
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb12y)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)
                                    idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                                }
                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qb1인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    idn_2 = i
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb12y)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p)
                                    idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                                }
                            }

                        }
                    }

                }

                //옮기려는 말이 qb1이거나 cb1의 quantum말일때
                else if(selview_bgi == "qb1" || selview_bgi == "cb1"){

                    //이동하려는 위치에 cb2가 있는 경우
                    if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.base)){

                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2qb1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                    } else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2)
                            && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.base)){

                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2qb1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p)

                    }
                    //이동할 위치중에 qb2이 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb12y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb12p)

                    } else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    idn_2 = i
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb12y)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)
                                    idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                                }
                            }

                        }
                    } else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    idn_2 = i
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb12y)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p)
                                    idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                                }
                            }

                        }
                    }

                }

                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                if(t1_1.text == "" && t1_2.text == "" && t1_3.text == "" && t1_4.text == "") {
                    check()
                    t1_4.text = "" + checknumber
                    if(checknumber == 0){
                        Toast.makeText(this@MainActivity,playername1+"의 승리입니다",Toast.LENGTH_LONG).show()
                        n=3
                        m=3
                        s1=0
                        selview_bgi = ""
                    }
                    else{
                        n=3
                        m=0
                        s1=0
                        selview_bgi = ""
                        checknumber = 0
                        t1_4.text = ""
                        if(n==2) {
                            text1 = ""
                            qt1_1 = ""
                            qt1_2 = ""
                            s2 = 0
                            svn = 0
                            idn = 0
                            idn_2 = 0
                        }
                    }
                }
            }

            // idnview 나 idn_2view에 상대방 말이 있어서 잡을 때
            else if(capture_bgi == "상대편말" || capture_bgi2 == "상대편말"){

                final()
                //옮기는 말이 qb1이거나 cb1이 quantum으로 이동하는 경우
                if(selview_bgi == "qb1" || selview_bgi == "cb1"){

                    //이동할 위치에 cr1,cr2 가 있으면
                    if(((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2))
                            ||((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1))){

                        imageButton33.setBackgroundResource(R.drawable.cr1)
                        imageButton34.setBackgroundResource(R.drawable.cr2)

                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }
                    //이동할 위치중에 cr1 이 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1)
                            || (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1)){

                        imageButton33.setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }

                    //이동할 위치중에 cr2 가 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2)
                            || (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2)){

                        imageButton34.setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }

                    //이동할 위치중에 qr1이 있으면
                    //둘다 qr1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1)){

                        imageButton33.setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }
                    //선택한데만 qr1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qr1p) // 잡힌것의 다른 쌍

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qr1인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qr1p) // 잡힌 말의 다른 쌍

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }

                    //이동할 위치중에 qr2가 있으면
                    //둘다 qr2인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        imageButton34.setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }
                    //선택한데만 qr2인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qr2p) // 잡힌것의 다른 쌍

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qr2인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qr2p) // 잡힌 말의 다른 쌍

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }

                    //이동할 위치중에 cr1qr2y 와 qr2p 쌍이 있으면
                    //둘다 그 쌍인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2p)) {

                        imageButton34.setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if (n2 == 1) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            } else if (n2 == 2) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                            }

                        }, 1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        }, 1800)

                    }
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2p)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){

                        imageButton34.setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }
                    //한군데만 qr2p인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2p)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr12)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr2p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2p)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr12)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }
                    //한군데만 cr1qr2y인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr2p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }

                    //이동할 위치중에 cr2qr2y 와 qr1p 쌍이 있으면
                    //둘다 그 쌍인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1p)) {

                        imageButton34.setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if (n2 == 1) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            } else if (n2 == 2) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                            }

                        }, 1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        }, 1800)

                    }
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1p)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){

                        imageButton34.setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }
                    //한군데만 qr1p인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1p)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr12)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr1p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1p)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr12)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }
                    //한군데만 cr2qr1y인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 cr2qr1y인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }

                }

                //옮기는 말이 qb2이거나 cb2이 quantum으로 이동하는 경우
                else if(selview_bgi == "qb2" || selview_bgi == "cb2"){

                    //이동할 위치에 cr1,cr2 가 있으면
                    if(((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2))
                            ||((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1))){

                        imageButton33.setBackgroundResource(R.drawable.cr1)
                        imageButton34.setBackgroundResource(R.drawable.cr2)

                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }
                    //이동할 위치중에 cr1 이 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1)
                            || (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1)){

                        imageButton33.setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }

                    //이동할 위치중에 cr2 가 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2)
                            || (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2)){

                        imageButton34.setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }

                    //이동할 위치중에 qr1이 있으면
                    //둘다 qr1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1)){

                        imageButton33.setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }
                    //선택한데만 qr1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qr1p) // 잡힌것의 다른 쌍

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qr1인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qr1p) // 잡힌 말의 다른 쌍

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }

                    //이동할 위치중에 qr2가 있으면
                    //둘다 qr2인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        imageButton34.setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }
                    //선택한데만 qr2인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qr2p) // 잡힌것의 다른 쌍

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qr2인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qr2p) // 잡힌 말의 다른 쌍

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }

                    //이동할 위치중에 cr1qr2y 와 qr2p 쌍이 있으면
                    //둘다 그 쌍인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2p)) {

                        imageButton34.setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if (n2 == 1) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            } else if (n2 == 2) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                            }

                        }, 1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        }, 1800)

                    }
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2p)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){

                        imageButton34.setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }
                    //한군데만 qr2p인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2p)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr12)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr2p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2p)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr12)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }
                    //한군데만 cr1qr2y인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr2p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }

                    //이동할 위치중에 cr2qr2y 와 qr1p 쌍이 있으면
                    //둘다 그 쌍인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1p)) {

                        imageButton34.setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if (n2 == 1) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            } else if (n2 == 2) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                            }

                        }, 1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        }, 1800)

                    }
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1p)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){

                        imageButton34.setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)

                    }
                    //한군데만 qr1p인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1p)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr12)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr1p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1p)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr2)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr12)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }
                    //한군데만 cr2qr1y인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton33.setBackgroundResource(R.drawable.cr1)
                                        imageButton34.setBackgroundResource(R.drawable.cr2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll1()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 cr2qr1y인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton33.setBackgroundResource(R.drawable.cr1)
                                imageButton34.setBackgroundResource(R.drawable.cr2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cr1)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll1()
                        },1800)


                    }



                }

                n=1
                sel = 0
                s2=0

            }
        }


    }

    //player2가 classic 말 이동 선택하는 경우
    fun classicmove2(){
        (thisview as CustomImageButton).apply{

            //이동시키고싶은 말 선택
            if (sameCheck2(R.drawable.select)) {
                //selview 는 이동하기위해 선택한 버튼, svn은 이동하기위해 선택한 버튼 번호
                if(svn == 0){
                    selview = thisview
                    svn = (selview as ImageButton)!!.context.resources.getResourceEntryName((selview as ImageButton)!!.id).substring(11).toInt()
                    if (sameCheck(R.drawable.cr1)) {
                        selview_bgi = "cr1"
                    } else if (sameCheck(R.drawable.cr2)) {
                        selview_bgi = "cr2"
                    } else if (sameCheck(R.drawable.cr12)) {
                        selview_bgi = "cr12"
                    } else if (sameCheck(R.drawable.cr1qr2y)) {
                        selview_bgi = "cr1qr2y"
                    } else if (sameCheck(R.drawable.cr1qr2p)) {
                        selview_bgi = "cr1qr2p"
                    } else if (sameCheck(R.drawable.cr2qr1y)) {
                        selview_bgi = "cr2qr1y"
                    } else if (sameCheck(R.drawable.cr2qr1p)) {
                        selview_bgi = "cr2qr1p"
                    } else if (sameCheck(R.drawable.qr1)) {
                        selview_bgi = "qr1"
                    } else if (sameCheck(R.drawable.qr1y)) {
                        selview_bgi = "qr1y"
                    } else if (sameCheck(R.drawable.qr1p)) {
                        selview_bgi = "qr1p"
                    } else if (sameCheck(R.drawable.qr2)) {
                        selview_bgi = "qr2"
                    } else if (sameCheck(R.drawable.qr2y)) {
                        selview_bgi = "qr2y"
                    } else if (sameCheck(R.drawable.qr2p)) {
                        selview_bgi = "qr2p"
                    } else if (sameCheck(R.drawable.qr12y)) {
                        selview_bgi = "qr12y"
                    } else if (sameCheck(R.drawable.qr12p)) {
                        selview_bgi = "qr12p"
                    }

                    showselect6()
                }
                else if(svn != 0){

                    delsel()
                    showselect4()
                    svn = 0
                    svn1 = 0
                    idn = 0
                    idn_2 = 0

                }

            }

            //해당 말이 이동할 위치 선택
            else if(sameCheck2(R.drawable.select2)){

                //quantum화된 애들을 옮기는 경우
                if(selview_bgi.substring(0,1) == "q"
                        || selview_bgi.substring(selview_bgi.length -1) == "p" || selview_bgi.substring(selview_bgi.length -1) == "y"){

                    subcm2()

                }
                //classic 말을 classic으로 옮기는 경우
                else {
                    //내가 가고자하는 곳에 이미 상대편 classic 말이 있으면 잡고 한번 더 돌림
                    if(sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2)||sameCheck(R.drawable.cb12)){
                        //잡은 말 정리
                        if(sameCheck(R.drawable.cb1)){
                            capture_bgi = "cb1"
                            imageButton29.setBackgroundResource(R.drawable.cb1)
                        }
                        else if(sameCheck(R.drawable.cb2)){
                            capture_bgi = "cb2"
                            imageButton30.setBackgroundResource(R.drawable.cb2)
                        }
                        else if(sameCheck(R.drawable.cb12)){
                            capture_bgi = "cb12"
                            imageButton29.setBackgroundResource(R.drawable.cb1)
                            imageButton30.setBackgroundResource(R.drawable.cb2)
                        }

                        //선택표시창 지우고, 원래 말 지우고, 선택관련 변수 초기화
                        final2()
                        //새 위치에 말 이동하기
                        if(selview_bgi == "cr1"){
                            setBackgroundResource(R.drawable.cr1)
                        }
                        else if(selview_bgi == "cr2"){
                            setBackgroundResource(R.drawable.cr2)
                        }
                        else if(selview_bgi == "cr12"){
                            setBackgroundResource(R.drawable.cr12)
                        }

                        //잡았으니까 다시 돌리고 윷/모 나오면 한번 더 진행
                        firstroll2()
                        val handler = Handler()
                        handler.postDelayed({
                            if(text2 == "윷" || text2 == "모"){
                                firstroll2()
                                val handler = Handler()
                                handler.postDelayed({
                                    if(text2 == "윷" || text2 == "모"){
                                        firstroll2()
                                    }
                                },3000)
                            }
                        },1500)
                    }

                    //선택한 곳에 내 classic 말이 있으면 업기
                    else if(sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2)){
                        //원래 있던 말 정리
                        if(sameCheck(R.drawable.cr1)){
                            capture_bgi = "cr1"
                        }
                        else if(sameCheck(R.drawable.cr2)){
                            capture_bgi = "cr2"
                        }


                        final2()
                        //새 위치에 말 이동하기
                        if((selview_bgi == "cr1" && capture_bgi == "cr2")||(selview_bgi =="cr2" && capture_bgi == "cr1")){
                            setBackgroundResource(R.drawable.cr12)
                        }

                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                            check2()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                d1=0
                            }
                            else{
                                m=3
                                n=0
                                d1=0
                                d2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""
                                capture_bgi2 = ""
                                capture_bgi3 = ""
                                checknumber = 0
                                t2_4.text = ""
                            }
                        }
                    }

                    //선택한 곳이 비어있는곳이면 그냥 옮기기
                    else if(sameCheck(R.drawable.base)){
                        //선택표시창 지우고, 원래 말 지우고, 선택관련 변수 초기화
                        final2()
                        //새 위치에 classic 말 이동하기
                        if(selview_bgi == "cr1"){
                            setBackgroundResource(R.drawable.cr1)
                        } else if(selview_bgi == "cr2"){
                            setBackgroundResource(R.drawable.cr2)
                        } else if(selview_bgi == "cr12"){
                            setBackgroundResource(R.drawable.cr12)
                        }

                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                            check2()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                d1=0
                            }
                            else{
                                m=3
                                n=0
                                d1=0
                                d2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""
                                capture_bgi2 = ""
                                capture_bgi3 = ""
                                checknumber = 0
                                t2_4.text = ""
                            }
                        }
                    }

                    //선택한 곳에 내 quantum 말이 있는 경우
                    else if(sameCheck(R.drawable.qr1)||sameCheck(R.drawable.qr2)){

                        if(sameCheck(R.drawable.qr1) && selview_bgi == "cr2"){

                            capture_bgi = "qr1"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.qr2) && selview_bgi == "cr1"){

                            capture_bgi = "qr2"
                            subcm2()

                        }

                    }

                    //선택한 곳에 상대방 quantum 말이 있는 경우
                    else if(sameCheck(R.drawable.qb1)||sameCheck(R.drawable.qb1y)||sameCheck(R.drawable.qb1p)
                            ||sameCheck(R.drawable.qb2)||sameCheck(R.drawable.qb2y)||sameCheck(R.drawable.qb2p)
                            ||sameCheck(R.drawable.qb12y)||sameCheck(R.drawable.qb12p)
                            ||sameCheck(R.drawable.cb1qb2y)||sameCheck(R.drawable.cb1qb2p)
                            ||sameCheck(R.drawable.cb2qb1y)||sameCheck(R.drawable.cb2qb1p)){

                        if(sameCheck(R.drawable.qb1)){

                            capture_bgi = "qb1"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.qb1y)){

                            capture_bgi = "qb1y"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.qb1p)){

                            capture_bgi = "qb1p"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.qb2)){

                            capture_bgi = "qb2"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.qb2p)){

                            capture_bgi = "qb2p"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.qb2y)){

                            capture_bgi = "qb2y"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.qb12y)){

                            capture_bgi = "qb12y"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.qb12p)){

                            capture_bgi = "qb12p"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.cb1qb2y)){

                            capture_bgi = "cb1qb2y"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.cb1qb2p)){

                            capture_bgi = "cb1qb2p"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.cb2qb1y)){

                            capture_bgi = "cb2qb1y"
                            subcm2()

                        }
                        else if(sameCheck(R.drawable.cb2qb1p)){

                            capture_bgi = "cb2qb1p"
                            subcm2()

                        }

                    }

                }



            }


        }

    }

    fun subcm2(){

        final2()

        //classic 말을 classic으로 옮길 위치에 quantum이 있을 때
        if(m==1 && selview_bgi.substring(0,1) == "c"){

            //classic이 이동하려고 선택한 곳에 내 quantum 말이 있는 경우
            if(capture_bgi == "qr1" || capture_bgi == "qr2"){

                capture = (thisview as ImageButton)!!.context.resources.getResourceEntryName((thisview as ImageButton)!!.id).substring(11).toInt()

                if(capture_bgi == "qr1"){
                    for(i in 0..28){
                        if(i != capture) { //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                            val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                            if ((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                capture2 = i

                            }
                        }
                    }

                    captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                    capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2qr1y)
                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr1p)

                }
                else if(capture_bgi == "qr2"){
                    for(i in 0..28){
                        if(i != capture) { //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                            val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                            if ((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                capture2 = i

                            }
                        }
                    }

                    captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                    capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1qr2y)
                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.qr2p)

                }

                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                    check2()
                    if(checknumber == 0){
                        Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                        n=3
                        m=3
                        d1=0
                    }
                    else{
                        m=3
                        n=0
                        d1=0
                        d2 = 0
                        idn = 0
                        idn_2 = 0
                        idn2 = 0
                        sel = 0
                        svn = 0
                        svn1 = 0
                        svn2 = 0
                        capture = 0
                        captureview = 0
                        capture2 = 0
                        capture2view = 0
                        capture3 = 0
                        capture3view = 0
                        selview_bgi = ""
                        capture_bgi = ""
                        capture_bgi2 = ""
                        capture_bgi3 = ""
                        checknumber = 0
                        t2_4.text = ""
                    }
                }

            }

            //classic이 이동하려고 선택한 곳에 상대방 quantum 말이 있는 경우
            else if(capture_bgi == "qb1"||capture_bgi == "qb1y"||capture_bgi == "qb1p"
                    ||capture_bgi == "qb2"||capture_bgi == "qb2y"||capture_bgi == "qb2p"
                    ||capture_bgi == "qb12y"||capture_bgi == "qb12p"
                    ||capture_bgi == "cb1qb2y"||capture_bgi == "cb1qb2p"
                    ||capture_bgi == "cb2qb1y"||capture_bgi == "cb2qb1p"){

                //선택한 곳 번호 확인(상대방 quantum말이 있는 곳)
                capture = (thisview as ImageButton)!!.context.resources.getResourceEntryName((thisview as ImageButton)!!.id).substring(11).toInt()

                if(selview_bgi == "cr1"){

                    if(capture_bgi == "qb1"){
                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },2000)


                    }
                    else if(capture_bgi == "qb1p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 !=0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)
                            findViewById<View>(capture3view).setBackgroundResource(R.drawable.qb2p)


                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb2)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)



                        }
                        //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                        else if(capture2 == 0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2qb1y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qb2"){
                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },2000)


                    }
                    else if(capture_bgi == "qb2p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 !=0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)
                            findViewById<View>(capture3view).setBackgroundResource(R.drawable.qb1p)


                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)



                        }
                        //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                        else if(capture2 == 0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1qb2y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qb12y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr12y가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 ==0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }


                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb2)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)



                        }
                        //잡은 qr12y가 qr12y, qr12p 쌍이면
                        else if(capture2 != 0){

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12p)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qb12p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        //잡은 qr12p가 qr12y, qr12p 쌍이면
                        if(capture2 != 0){

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "cb1qb2y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2p)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },2000)

                    }
                    else if(capture_bgi == "cb2qb1y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1p)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },2000)

                    }

                }
                else if(selview_bgi == "cr2"){

                    if(capture_bgi == "qb1"){
                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },2000)


                    }
                    else if(capture_bgi == "qb1p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 !=0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)
                            findViewById<View>(capture3view).setBackgroundResource(R.drawable.qb2p)


                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb2)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)



                        }
                        //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                        else if(capture2 == 0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2qb1y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qb2"){
                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },2000)


                    }
                    else if(capture_bgi == "qb2p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 !=0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)
                            findViewById<View>(capture3view).setBackgroundResource(R.drawable.qb1p)


                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)



                        }
                        //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                        else if(capture2 == 0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1qb2y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qb12y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }
                        //잡은 qr12y가 qr12y, qr2p, qr1p 쌍이면
                        if(capture2 ==0){
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            for(i in 0..28){
                                if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture3 = i

                                    }
                                }
                            }


                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                            capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb2)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)



                        }
                        //잡은 qr12y가 qr12y, qr12p 쌍이면
                        else if(capture2 != 0){

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12p)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "qb12p"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        //잡은 qr12p가 qr12y, qr12p 쌍이면
                        if(capture2 != 0){

                            captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)

                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }
                    else if(capture_bgi == "cb1qb2y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2p)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },2000)

                    }
                    else if(capture_bgi == "cb2qb1y"){

                        capture2 = 0

                        for(i in 0..28){
                            if(i != capture){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                    capture2 = i

                                }
                            }
                        }

                        captureview = resources.getIdentifier("imageButton" + capture, "id", packageName)
                        capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1p)

                        (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                        (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                            if(n2 == 1){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },2000)

                    }


                }

            }

            //classic에 quantum 업힌게 이동하는 경우
            else if(selview_bgi.substring(selview_bgi.length -1) == "p" || selview_bgi.substring(selview_bgi.length -1) == "y"){

                if(selview_bgi == "cr1qr2y"){
                    //svn : cb1qb2y 위치, idn : cb1qb2y 가 이동할 위치, svn1 : qb2p 위치, idn_2 : qb2p가 이동할 위치
                    //이동할 두 곳에 뭐가 있는지 확인
                    (findViewById<View>(idnview) as CustomImageButton).apply{
                        //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                        if(sameCheck(R.drawable.base)){
                            capture_bgi = "base"
                        }
                        else if(sameCheck(R.drawable.cb1)){
                            capture_bgi = "cb1"
                        }
                        else if(sameCheck(R.drawable.cb2)){
                            capture_bgi = "cb2"
                        }
                        else if(sameCheck(R.drawable.cb12)){
                            capture_bgi = "cb12"
                        }
                        else if(sameCheck(R.drawable.qb1)){
                            capture_bgi = "qb1"
                        }
                        else if(sameCheck(R.drawable.qb1p)){
                            capture_bgi = "qb1p"
                        }
                        else if(sameCheck(R.drawable.qb2)){
                            capture_bgi = "qb2"
                        }
                        else if(sameCheck(R.drawable.qb2p)){
                            capture_bgi = "qb2p"
                        }
                        else if(sameCheck(R.drawable.qb12y)){
                            capture_bgi = "qb12y"
                        }
                        else if(sameCheck(R.drawable.qb12p)){
                            capture_bgi = "qb12p"
                        }
                        else if(sameCheck(R.drawable.cb1qb2y)){
                            capture_bgi = "cb1qb2y"
                        }
                        else if(sameCheck(R.drawable.cb2qb1y)){
                            capture_bgi = "cb2qb1y"
                        }
                    }
                    (findViewById<View>(idn_2view) as CustomImageButton).apply{
                        //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                        if(sameCheck(R.drawable.base)){
                            capture_bgi2 = "base"
                        }
                        else if(sameCheck(R.drawable.cb1)){
                            capture_bgi2 = "cb1"
                        }
                        else if(sameCheck(R.drawable.cb2)){
                            capture_bgi2 = "cb2"
                        }
                        else if(sameCheck(R.drawable.cb12)){
                            capture_bgi2 = "cb12"
                        }
                        else if(sameCheck(R.drawable.qb1)){
                            capture_bgi2 = "qb1"
                        }
                        else if(sameCheck(R.drawable.qb1p)){
                            capture_bgi2 = "qb1p"
                        }
                        else if(sameCheck(R.drawable.qb2)){
                            capture_bgi2 = "qb2"
                        }
                        else if(sameCheck(R.drawable.qb2p)){
                            capture_bgi2 = "qb2p"
                        }
                        else if(sameCheck(R.drawable.qb12y)){
                            capture_bgi2 = "qb12y"
                        }
                        else if(sameCheck(R.drawable.qb12p)){
                            capture_bgi2 = "qb12p"
                        }
                        else if(sameCheck(R.drawable.cb1qb2y)){
                            capture_bgi2 = "cb1qb2y"
                        }
                        else if(sameCheck(R.drawable.cb2qb1y)){
                            capture_bgi2 = "cb2qb1y"
                        }
                    }

                    if(capture_bgi == "base" && capture_bgi2 == "base"){
                        final()
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1qr2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                            check2()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                d1=0
                            }
                            else{
                                m=3
                                n=0
                                d1=0
                                d2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""
                                capture_bgi2 = ""
                                capture_bgi3 = ""
                                checknumber = 0
                                t2_4.text = ""
                            }
                        }


                    }

                    else if(capture_bgi == "base" && capture_bgi2 != "base" ){
                        //선택한 곳 번호 확인(상대방 quantum말이 있는 곳)
                        capture = (thisview as ImageButton)!!.context.resources.getResourceEntryName((thisview as ImageButton)!!.id).substring(11).toInt()

                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1qr2y)

                        if(capture_bgi2 == "qb1"){
                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)


                        }
                        else if(capture_bgi2 == "qb1p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 !=0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)
                                findViewById<View>(capture3view).setBackgroundResource(R.drawable.qb2p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb2)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)



                            }
                            //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                            else if(capture2 == 0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2qb1y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qb2"){
                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)


                        }
                        else if(capture_bgi2 == "qb2p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 !=0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)
                                findViewById<View>(capture3view).setBackgroundResource(R.drawable.qb1p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)



                            }
                            //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                            else if(capture2 == 0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1qb2y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qb12y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr12y가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 ==0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }


                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb2)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)



                            }
                            //잡은 qr12y가 qr12y, qr12p 쌍이면
                            else if(capture2 != 0){

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qb12p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            //잡은 qr12p가 qr12y, qr12p 쌍이면
                            if(capture2 != 0){

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "cb1qb2y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2p)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }
                        else if(capture_bgi2 == "cb2qb1y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qr2p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1p)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }


                }
                else if(selview_bgi == "cr2qr1y"){
                    //svn : cb1qb2y 위치, idn : cb1qb2y 가 이동할 위치, svn1 : qb2p 위치, idn_2 : qb2p가 이동할 위치
                    //이동할 두 곳에 뭐가 있는지 확인
                    (findViewById<View>(idnview) as CustomImageButton).apply{
                        //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                        if(sameCheck(R.drawable.base)){
                            capture_bgi = "base"
                        }
                        else if(sameCheck(R.drawable.cb1)){
                            capture_bgi = "cb1"
                        }
                        else if(sameCheck(R.drawable.cb2)){
                            capture_bgi = "cb2"
                        }
                        else if(sameCheck(R.drawable.cb12)){
                            capture_bgi = "cb12"
                        }
                        else if(sameCheck(R.drawable.qb1)){
                            capture_bgi = "qb1"
                        }
                        else if(sameCheck(R.drawable.qb1p)){
                            capture_bgi = "qb1p"
                        }
                        else if(sameCheck(R.drawable.qb2)){
                            capture_bgi = "qb2"
                        }
                        else if(sameCheck(R.drawable.qb2p)){
                            capture_bgi = "qb2p"
                        }
                        else if(sameCheck(R.drawable.qb12y)){
                            capture_bgi = "qb12y"
                        }
                        else if(sameCheck(R.drawable.qb12p)){
                            capture_bgi = "qb12p"
                        }
                        else if(sameCheck(R.drawable.cb1qb2y)){
                            capture_bgi = "cb1qb2y"
                        }
                        else if(sameCheck(R.drawable.cb2qb1y)){
                            capture_bgi = "cb2qb1y"
                        }
                    }
                    (findViewById<View>(idn_2view) as CustomImageButton).apply{
                        //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                        if(sameCheck(R.drawable.base)){
                            capture_bgi2 = "base"
                        }
                        else if(sameCheck(R.drawable.cb1)){
                            capture_bgi2 = "cb1"
                        }
                        else if(sameCheck(R.drawable.cb2)){
                            capture_bgi2 = "cb2"
                        }
                        else if(sameCheck(R.drawable.cb12)){
                            capture_bgi2 = "cb12"
                        }
                        else if(sameCheck(R.drawable.qb1)){
                            capture_bgi2 = "qb1"
                        }
                        else if(sameCheck(R.drawable.qb1p)){
                            capture_bgi2 = "qb1p"
                        }
                        else if(sameCheck(R.drawable.qb2)){
                            capture_bgi2 = "qb2"
                        }
                        else if(sameCheck(R.drawable.qb2p)){
                            capture_bgi2 = "qb2p"
                        }
                        else if(sameCheck(R.drawable.qb12y)){
                            capture_bgi2 = "qb12y"
                        }
                        else if(sameCheck(R.drawable.qb12p)){
                            capture_bgi2 = "qb12p"
                        }
                        else if(sameCheck(R.drawable.cb1qb2y)){
                            capture_bgi2 = "cb1qb2y"
                        }
                        else if(sameCheck(R.drawable.cb2qb1y)){
                            capture_bgi2 = "cb2qb1y"
                        }
                    }

                    if(capture_bgi == "base" && capture_bgi2 == "base"){
                        final()
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2qr1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                            check2()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                d1=0
                            }
                            else{
                                m=3
                                n=0
                                d1=0
                                d2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""
                                capture_bgi2 = ""
                                capture_bgi3 = ""
                                checknumber = 0
                                t2_4.text = ""
                            }
                        }


                    }

                    else if(capture_bgi == "base" && capture_bgi2 != "base" ){
                        //선택한 곳 번호 확인(상대방 quantum말이 있는 곳)
                        capture = (thisview as ImageButton)!!.context.resources.getResourceEntryName((thisview as ImageButton)!!.id).substring(11).toInt()

                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2qr1y)

                        if(capture_bgi2 == "qb1"){
                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)


                        }
                        else if(capture_bgi2 == "qb1p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 !=0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)
                                findViewById<View>(capture3view).setBackgroundResource(R.drawable.qb2p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb2)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)



                            }
                            //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                            else if(capture2 == 0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2qb1y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qb2"){
                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)

                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)


                        }
                        else if(capture_bgi2 == "qb2p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr1p가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 !=0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)
                                findViewById<View>(capture3view).setBackgroundResource(R.drawable.qb1p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)



                            }
                            //잡은 qr1p가 cr2qr1y, qr1p 쌍이면
                            else if(capture2 == 0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1qb2y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qb12y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }
                            //잡은 qr12y가 qr12y, qr2p, qr1p 쌍이면
                            if(capture2 ==0){
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture2 = i

                                        }
                                    }
                                }
                                for(i in 0..28){
                                    if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                            capture3 = i

                                        }
                                    }
                                }


                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)
                                capture3view = resources.getIdentifier("imageButton" + capture3, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture3view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture3view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.cb2)

                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture3view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)



                            }
                            //잡은 qr12y가 qr12y, qr12p 쌍이면
                            else if(capture2 != 0){

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "qb12p"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            //잡은 qr12p가 qr12y, qr12p 쌍이면
                            if(capture2 != 0){

                                captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                                findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                                findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb12y)

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                    (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb12)
                                    }
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },2000)

                            }

                        }
                        else if(capture_bgi2 == "cb1qb2y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb2p)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb2)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }
                        else if(capture_bgi2 == "cb2qb1y"){

                            capture2 = 0

                            for(i in 0..28){
                                if(i != idn_2){ //이동할 위치에 qr1이 있을때 다른 위치에 있는 qr1 찾기
                                    val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                    if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qr1인 말 번호

                                        capture2 = i

                                    }
                                }
                            }

                            captureview = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                            capture2view = resources.getIdentifier("imageButton" + capture2, "id", packageName)

                            findViewById<View>(captureview).setBackgroundResource(R.drawable.qr1p)
                            findViewById<View>(capture2view).setBackgroundResource(R.drawable.qb1p)

                            (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(captureview) as CustomImageButton).setImageResource(R.drawable.select2)
                            (findViewById<View>(capture2view) as CustomImageButton).setImageResource(R.drawable.select2)

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()

                                (findViewById<View>(idnview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(captureview) as CustomImageButton).setImageResource(android.R.color.transparent)
                                (findViewById<View>(capture2view) as CustomImageButton).setImageResource(android.R.color.transparent)

                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cb2)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.cb1)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(captureview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(capture2view).setBackgroundResource(R.drawable.base)
                                    imageButton29.setBackgroundResource(R.drawable.cb1)
                                    imageButton30.setBackgroundResource(R.drawable.cb2)
                                }

                            },1500)

                            //잡았으니까 다시 돌림
                            handler.postDelayed({
                                firstroll2()
                            },2000)

                        }

                    }


                }


            }

        }

        //quantum 말을 classic으로 옮기거나 classic 말을 quantum으로 옮길때, quantum 말이 어떻게 놓여지는지에 대한 코드
        else if(m==2 || selview_bgi.substring(0,1) == "q"){

            //이동할 두 곳에 뭐가 있는지 확인
            (findViewById<View>(idnview) as CustomImageButton).apply{
                //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                if(sameCheck(R.drawable.base)){capture_bgi = "빈칸"}
                else if(sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2)||sameCheck(R.drawable.cb12)
                        ||sameCheck(R.drawable.qb1)||sameCheck(R.drawable.qb2)
                        ||sameCheck(R.drawable.qb1y)||sameCheck(R.drawable.qb2y)||sameCheck(R.drawable.qb12y)
                        ||sameCheck(R.drawable.qb1p)||sameCheck(R.drawable.qb2p)||sameCheck(R.drawable.qb12p)
                        ||sameCheck(R.drawable.cb1qb2y)||sameCheck(R.drawable.cb2qb1y)){capture_bgi = "상대편말"}
                else if(sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2)||sameCheck(R.drawable.cr12)
                        ||sameCheck(R.drawable.qr1)||sameCheck(R.drawable.qr2)
                        ||sameCheck(R.drawable.qr1y)||sameCheck(R.drawable.qr2y)||sameCheck(R.drawable.qr12y)
                        ||sameCheck(R.drawable.qr1p)||sameCheck(R.drawable.qr2p)||sameCheck(R.drawable.qr12p)
                        ||sameCheck(R.drawable.cr1qr2y)||sameCheck(R.drawable.cr2qr1y)){capture_bgi ="내말"}
            }
            (findViewById<View>(idn_2view) as CustomImageButton).apply{
                //비어있으면 0, 내 말이 있으면 1, 상대방 말이 있으면 2로 표시
                if(sameCheck(R.drawable.base)){capture_bgi2 = "빈칸"}
                else if(sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2)||sameCheck(R.drawable.cb12)
                        ||sameCheck(R.drawable.qb1)||sameCheck(R.drawable.qb2)
                        ||sameCheck(R.drawable.qb1y)||sameCheck(R.drawable.qb2y) ||sameCheck(R.drawable.qb12y)
                        ||sameCheck(R.drawable.qb1p)||sameCheck(R.drawable.qb2p) ||sameCheck(R.drawable.qb12p)
                        ||sameCheck(R.drawable.cb1qb2y)||sameCheck(R.drawable.cb2qb1y)){capture_bgi2 = "상대편말"}
                else if(sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2) ||sameCheck(R.drawable.cr12)
                        ||sameCheck(R.drawable.qr1)||sameCheck(R.drawable.qr2)
                        ||sameCheck(R.drawable.qr1y)||sameCheck(R.drawable.qr2y) ||sameCheck(R.drawable.qr12y)
                        ||sameCheck(R.drawable.qr1p)||sameCheck(R.drawable.qr2p) ||sameCheck(R.drawable.qr12p)
                        ||sameCheck(R.drawable.cr1qr2y)||sameCheck(R.drawable.cr2qr1y)){capture_bgi2 ="내말"}
            }


            //이동하고자 하는 위치 두 곳 다 비어있을때
            if(capture_bgi == "빈칸" && capture_bgi2 == "빈칸"){

                if(selview_bgi == "qr1" || selview_bgi == "cr1"){

                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1)

                    if(idn == 0 || idn_2 == 0){

                        if(idn == 0 && idn_2 == 0){
                            findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)

                            //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                            if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                                check2()
                                if(checknumber == 0){
                                    Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                    n=3
                                    m=3
                                    d1=0
                                }
                                else{
                                    m=3
                                    n=0
                                    d1=0
                                    d2 = 0
                                    idn = 0
                                    idn_2 = 0
                                    idn2 = 0
                                    sel = 0
                                    svn = 0
                                    svn1 = 0
                                    svn2 = 0
                                    capture = 0
                                    captureview = 0
                                    capture2 = 0
                                    capture2view = 0
                                    capture3 = 0
                                    capture3view = 0
                                    selview_bgi = ""
                                    capture_bgi = ""
                                    capture_bgi2 = ""
                                    capture_bgi3 = ""
                                    checknumber = 0
                                    t2_4.text = ""
                                }
                            }

                        } else{ // 하나만 홈에 도착하면 확률선택으로 결정됨

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()


                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                }

                                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                                if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                                    check2()
                                    if(checknumber == 0){
                                        Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                        n=3
                                        m=3
                                        d1=0
                                    }
                                    else{
                                        m=3
                                        n=0
                                        d1=0
                                        d2 = 0
                                        idn = 0
                                        idn_2 = 0
                                        idn2 = 0
                                        sel = 0
                                        svn = 0
                                        svn1 = 0
                                        svn2 = 0
                                        capture = 0
                                        captureview = 0
                                        capture2 = 0
                                        capture2view = 0
                                        capture3 = 0
                                        capture3view = 0
                                        selview_bgi = ""
                                        capture_bgi = ""
                                        capture_bgi2 = ""
                                        capture_bgi3 = ""
                                        checknumber = 0
                                        t2_4.text = ""
                                    }
                                }

                            },1500)
                        }
                    }
                    else{
                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                            check2()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                d1=0
                            }
                            else{
                                m=3
                                n=0
                                d1=0
                                d2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""
                                capture_bgi2 = ""
                                capture_bgi3 = ""
                                checknumber = 0
                                t2_4.text = ""
                            }
                        }
                    }

                }
                else if(selview_bgi == "qr2" || selview_bgi == "cr2"){
                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2)

                    if(idn == 0 || idn_2 == 0){

                        if(idn == 0 && idn_2 == 0){
                            findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)

                            //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                            if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                                check2()
                                if(checknumber == 0){
                                    Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                    n=3
                                    m=3
                                    d1=0
                                }
                                else{
                                    m=3
                                    n=0
                                    d1=0
                                    d2 = 0
                                    idn = 0
                                    idn_2 = 0
                                    idn2 = 0
                                    sel = 0
                                    svn = 0
                                    svn1 = 0
                                    svn2 = 0
                                    capture = 0
                                    captureview = 0
                                    capture2 = 0
                                    capture2view = 0
                                    capture3 = 0
                                    capture3view = 0
                                    selview_bgi = ""
                                    capture_bgi = ""
                                    capture_bgi2 = ""
                                    capture_bgi3 = ""
                                    checknumber = 0
                                    t2_4.text = ""
                                }
                            }

                        } else{ // 하나만 홈에 도착하면 확률선택으로 결정됨

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()


                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                }

                                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                                if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                                    check2()
                                    if(checknumber == 0){
                                        Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                        n=3
                                        m=3
                                        d1=0
                                    }
                                    else{
                                        m=3
                                        n=0
                                        d1=0
                                        d2 = 0
                                        idn = 0
                                        idn_2 = 0
                                        idn2 = 0
                                        sel = 0
                                        svn = 0
                                        svn1 = 0
                                        svn2 = 0
                                        capture = 0
                                        captureview = 0
                                        capture2 = 0
                                        capture2view = 0
                                        capture3 = 0
                                        capture3view = 0
                                        selview_bgi = ""
                                        capture_bgi = ""
                                        capture_bgi2 = ""
                                        capture_bgi3 = ""
                                        checknumber = 0
                                        t2_4.text = ""
                                    }
                                }

                            },1500)
                        }
                    }
                    else{
                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                            check2()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                d1=0
                            }
                            else{
                                m=3
                                n=0
                                d1=0
                                d2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""
                                capture_bgi2 = ""
                                capture_bgi3 = ""
                                checknumber = 0
                                t2_4.text = ""
                            }
                        }
                    }

                }
                else if(selview_bgi == "qr12y" || selview_bgi == "qr12p" || selview_bgi == "cr12"){
                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr12y)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr12p)

                    if(idn == 0 || idn_2 == 0){

                        if(idn == 0 && idn_2 == 0){
                            findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)

                            //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                            if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                                check2()
                                if(checknumber == 0){
                                    Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                    n=3
                                    m=3
                                    d1=0
                                }
                                else{
                                    m=3
                                    n=0
                                    d1=0
                                    d2 = 0
                                    idn = 0
                                    idn_2 = 0
                                    idn2 = 0
                                    sel = 0
                                    svn = 0
                                    svn1 = 0
                                    svn2 = 0
                                    capture = 0
                                    captureview = 0
                                    capture2 = 0
                                    capture2view = 0
                                    capture3 = 0
                                    capture3view = 0
                                    selview_bgi = ""
                                    capture_bgi = ""
                                    capture_bgi2 = ""
                                    capture_bgi3 = ""
                                    checknumber = 0
                                    t2_4.text = ""
                                }
                            }

                        } else{ // 하나만 홈에 도착하면 확률선택으로 결정됨

                            ani_loading.setVisibility(View.VISIBLE)
                            ani_loading.bringToFront()
                            ani_loading.invalidate()

                            val animation = ani_loading.background as AnimationDrawable
                            animation.start()

                            val handler = Handler()
                            handler.postDelayed({

                                n1 = (Math.random() * 2) + 1
                                n2 = n1.toInt()

                                ani_loading.setVisibility(View.GONE)
                                animation.stop()


                                if(n2 == 1){
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                }
                                else if(n2 == 2){
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr12)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                }

                                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                                if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                                    check2()
                                    if(checknumber == 0){
                                        Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                        n=3
                                        m=3
                                        d1=0
                                    }
                                    else{
                                        m=3
                                        n=0
                                        d1=0
                                        d2 = 0
                                        idn = 0
                                        idn_2 = 0
                                        idn2 = 0
                                        sel = 0
                                        svn = 0
                                        svn1 = 0
                                        svn2 = 0
                                        capture = 0
                                        captureview = 0
                                        capture2 = 0
                                        capture2view = 0
                                        capture3 = 0
                                        capture3view = 0
                                        selview_bgi = ""
                                        capture_bgi = ""
                                        capture_bgi2 = ""
                                        capture_bgi3 = ""
                                        checknumber = 0
                                        t2_4.text = ""
                                    }
                                }

                            },1500)
                        }
                    }
                    else{
                        //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                        if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                            check2()
                            if(checknumber == 0){
                                Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                                n=3
                                m=3
                                d1=0
                            }
                            else{
                                m=3
                                n=0
                                d1=0
                                d2 = 0
                                idn = 0
                                idn_2 = 0
                                idn2 = 0
                                sel = 0
                                svn = 0
                                svn1 = 0
                                svn2 = 0
                                capture = 0
                                captureview = 0
                                capture2 = 0
                                capture2view = 0
                                capture3 = 0
                                capture3view = 0
                                selview_bgi = ""
                                capture_bgi = ""
                                capture_bgi2 = ""
                                capture_bgi3 = ""
                                checknumber = 0
                                t2_4.text = ""
                            }
                        }
                    }

                }

            }

            // idnview 나 idn_2view에 내 다른 말이 있어서 업을 때
            else if(capture_bgi == "내말" || capture_bgi2 == "내말"){
                final2()
                //옮기는 말이 qb2거나 cb2의 quantum패 일때
                if(selview_bgi == "qr2" || selview_bgi == "cr2"){
                    //이동할 위치중에 cb1 이 있으면
                    if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.base)){
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1qr2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)
                    }
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr1)
                            && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.base)){
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1qr2y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p)
                    }
                    //이동할 위치중에 qb1이 있으면
                    //둘다 qb1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1)){
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr12y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr12p)
                    }
                    //선택한데만 qb1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    idn_2 = i
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr12y)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)
                                    idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                                }
                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qb1인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr1)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    idn_2 = i
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr12y)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p)
                                    idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                                }
                            }

                        }
                    }

                }

                //옮기려는 말이 qb1이거나 cb1의 quantum말일때
                else if(selview_bgi == "qr1" || selview_bgi == "cr1"){

                    //이동하려는 위치에 cb2가 있는 경우
                    if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cr2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.base)){

                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2qr1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                    } else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cr2)
                            && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.base)){

                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2qr1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p)

                    }
                    //이동할 위치중에 qb2이 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr12y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr12p)

                    } else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qr2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    idn_2 = i
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr12y)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)
                                    idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                                }
                            }

                        }
                    } else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qr2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    idn_2 = i
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr12y)
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p)
                                    idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                                }
                            }

                        }
                    }

                }

                //이것으로 player1의 턴이 끝났으면 player2가 할 수 있게 정리
                if(t2_1.text == "" && t2_2.text == "" && t2_3.text == "" && t2_4.text == "") {
                    check2()
                    if(checknumber == 0){
                        Toast.makeText(this@MainActivity,playername2+"의 승리입니다",Toast.LENGTH_LONG).show()
                        n=3
                        m=3
                        d1=0
                    }
                    else{
                        m=3
                        n=0
                        d1=0
                        d2 = 0
                        idn = 0
                        idn_2 = 0
                        idn2 = 0
                        sel = 0
                        svn = 0
                        svn1 = 0
                        svn2 = 0
                        capture = 0
                        captureview = 0
                        capture2 = 0
                        capture2view = 0
                        capture3 = 0
                        capture3view = 0
                        selview_bgi = ""
                        capture_bgi = ""
                        capture_bgi2 = ""
                        capture_bgi3 = ""
                        checknumber = 0
                        t2_4.text = ""
                    }
                }
            }

            // idnview 나 idn_2view에 상대방 말이 있어서 잡을 때
            else if(capture_bgi == "상대편말" || capture_bgi2 == "상대편말"){

                final2()
                //옮기는 말이 qb1이거나 cb1이 quantum으로 이동하는 경우
                if(selview_bgi == "qr1" || selview_bgi == "cr1"){

                    //이동할 위치에 cr1,cr2 가 있으면
                    if(((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2))
                            ||((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1))){

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        imageButton30.setBackgroundResource(R.drawable.cb2)

                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }
                    //이동할 위치중에 cr1 이 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1)
                            || (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1)){

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }

                    //이동할 위치중에 cr2 가 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2)
                            || (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2)){

                        imageButton30.setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }

                    //이동할 위치중에 qr1이 있으면
                    //둘다 qr1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }
                    //선택한데만 qr1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qb1p) // 잡힌것의 다른 쌍

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qr1인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qb1p) // 잡힌 말의 다른 쌍

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }

                    //이동할 위치중에 qr2가 있으면
                    //둘다 qr2인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        imageButton30.setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }
                    //선택한데만 qr2인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qb2p) // 잡힌것의 다른 쌍

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qr2인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qb2p) // 잡힌 말의 다른 쌍

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }

                    //이동할 위치중에 cr1qr2y 와 qr2p 쌍이 있으면
                    //둘다 그 쌍인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2p)) {

                        imageButton30.setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if (n2 == 1) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            } else if (n2 == 2) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                            }

                        }, 1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        }, 1800)

                    }
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2p)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){

                        imageButton30.setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }
                    //한군데만 qr2p인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2p)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb12)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr2p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2p)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb12)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }
                    //한군데만 cr1qr2y인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr2p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }

                    //이동할 위치중에 cr2qr2y 와 qr1p 쌍이 있으면
                    //둘다 그 쌍인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1p)) {

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if (n2 == 1) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb2)
                            } else if (n2 == 2) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                            }

                        }, 1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        }, 1800)

                    }
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1p)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }
                    //한군데만 qr1p인경우
                    //(181104 01:46 수정작업중)
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1p)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb12)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr1p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1p)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb12)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }
                    //한군데만 cr2qr1y인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 cr2qr1y인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }

                }

                //옮기는 말이 qb2이거나 cb2이 quantum으로 이동하는 경우
                else if(selview_bgi == "qr2" || selview_bgi == "cr2"){

                    //이동할 위치에 cr1,cr2 가 있으면
                    if(((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2))
                            ||((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1))){

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        imageButton30.setBackgroundResource(R.drawable.cb2)

                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }
                    //이동할 위치중에 cr1 이 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1)
                            || (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1)){

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }

                    //이동할 위치중에 cr2 가 있으면
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2)
                            || (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2)){

                        imageButton30.setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2y)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }

                    //이동할 위치중에 qr2이 있으면
                    //둘다 qr1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }
                    //선택한데만 qr1인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qb1p) // 잡힌것의 다른 쌍

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qr1인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qb1p) // 잡힌 말의 다른 쌍

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }

                    //이동할 위치중에 qr2가 있으면
                    //둘다 qr2인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        imageButton30.setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }
                    //선택한데만 qr2인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qb2p) // 잡힌것의 다른 쌍

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //선택한데 말고 다른 곳만 qr2인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
                                    findViewById<View>(idn2view).setBackgroundResource(R.drawable.qb2p) // 잡힌 말의 다른 쌍

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }

                    //이동할 위치중에 cr2qr2y 와 qr2p 쌍이 있으면
                    //둘다 그 쌍인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2p)) {

                        imageButton30.setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if (n2 == 1) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            } else if (n2 == 2) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                            }

                        }, 1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        }, 1800)

                    }
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2p)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){

                        imageButton30.setBackgroundResource(R.drawable.cb2)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }
                    //한군데만 qr2p인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2p)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb2p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb12)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr2p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2p)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb2p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb12)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }
                    //한군데만 cr1qr2y인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb1)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr2p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb1)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }

                    //이동할 위치중에 cr2qr2y 와 qr1p 쌍이 있으면
                    //둘다 그 쌍인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1p)) {

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if (n2 == 1) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb2)
                            } else if (n2 == 2) {
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                            }

                        }, 1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        }, 1800)

                    }
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1p)
                            && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){

                        imageButton29.setBackgroundResource(R.drawable.cb1)
                        findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y)
                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p)

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()


                            if(n2 == 1){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                            }
                            else if(n2 == 2){
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)

                    }
                    //한군데만 qr1p인경우
                    //(181104 01:46 수정작업중)
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1p)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.qb1p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb12)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 qr1p인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1p)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.qb1p)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb2)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb12)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }
                    //한군데만 cr2qr1y인경우
                    else if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){

                        idn = (findViewById<View>(idnview) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idnview) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2y) // 이동한 곳은 잡음
                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2p) // 다른 곳은 잡을것 없으니까 그냥 이동

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }

                                ani_loading.setVisibility(View.VISIBLE)
                                ani_loading.bringToFront()
                                ani_loading.invalidate()

                                val animation = ani_loading.background as AnimationDrawable
                                animation.start()

                                val handler = Handler()
                                handler.postDelayed({

                                    n1 = (Math.random() * 2) + 1
                                    n2 = n1.toInt()

                                    ani_loading.setVisibility(View.GONE)
                                    animation.stop()

                                    //잡은쪽만 남거나
                                    if(n2 == 1){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.base)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                        imageButton29.setBackgroundResource(R.drawable.cb1)
                                        imageButton30.setBackgroundResource(R.drawable.cb2)
                                    }
                                    //나머지들만 남거나
                                    else if(n2 == 2){
                                        findViewById<View>(idnview).setBackgroundResource(R.drawable.cb2)
                                        findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                        findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)

                                    }

                                },1500)

                                //잡았으니까 다시 돌림
                                handler.postDelayed({
                                    firstroll2()
                                },1800)

                            }

                        }
                    }
                    //다른 곳만 cr2qr1y인경우
                    else if((findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){

                        idn = (findViewById<View>(idn_2view) as ImageButton)!!.context.resources.getResourceEntryName((findViewById<View>(idn_2view) as ImageButton)!!.id).substring(11).toInt()
                        for(i in 0..28){
                            if(i != idn && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)){ //이동할 위치에 qb1이 있을때 다른 위치에 있는 qb1 찾기
                                val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                                if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) { //i는 idn과 다른 위치에 있으면서 qb1인 말 번호

                                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2y) // 잡은 곳
                                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2p) // 그냥 이동한 곳

                                    idn_2 = i
                                    idn2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                                }
                            }

                        }

                        ani_loading.setVisibility(View.VISIBLE)
                        ani_loading.bringToFront()
                        ani_loading.invalidate()

                        val animation = ani_loading.background as AnimationDrawable
                        animation.start()

                        val handler = Handler()
                        handler.postDelayed({

                            n1 = (Math.random() * 2) + 1
                            n2 = n1.toInt()

                            ani_loading.setVisibility(View.GONE)
                            animation.stop()

                            //잡은쪽만 남거나
                            if(n2 == 1){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.base)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.base)
                                imageButton29.setBackgroundResource(R.drawable.cb1)
                                imageButton30.setBackgroundResource(R.drawable.cb2)
                            }
                            //나머지들만 남거나
                            else if(n2 == 2){
                                findViewById<View>(idn_2view).setBackgroundResource(R.drawable.cb2)
                                findViewById<View>(idnview).setBackgroundResource(R.drawable.cr2)
                                findViewById<View>(idn2view).setBackgroundResource(R.drawable.cb1)

                            }

                        },1500)

                        //잡았으니까 다시 돌림
                        handler.postDelayed({
                            firstroll2()
                        },1800)


                    }

                }

                m=1
                sel = 0
                d2=0

            }
        }


    }

    //player1이 quantum으로 말 이동 선택하는 경우
    fun quantummove1(){
        (thisview as CustomImageButton).apply{

            //두개 패가 다르면 quantum move. 옮길 말 선택하면 그 말이 갈수있는 곳 표시
            if(sameCheck2(R.drawable.select) && selview_bgi == ""){
                delsel()
                setImageResource(R.drawable.select)
                selview = thisview
                svn = (selview as ImageButton)!!.context.resources.getResourceEntryName((selview as ImageButton)!!.id).substring(11).toInt()

                //옮길 말이 선택되면 그 말이 갈수 있는 곳 표시
                if (sameCheck(R.drawable.cb1)) {
                    selview_bgi = "cb"+1
                } else if (sameCheck(R.drawable.cb2)) {
                    selview_bgi = "cb"+2
                } else if (sameCheck(R.drawable.cb12)) {
                    selview_bgi = "cb"+12
                }

                showselect5()

            }
            //선택했던 말 다시 선택하면 선택취소
            else if(sameCheck2(R.drawable.select) && selview_bgi != ""){
                selview_bgi = ""
                delsel()
                showselect3()
            }

            //이동할 곳 선택 후 처리(showselect5 에서 이동할 수 있는 위치 두곳 idn 과 idn_2 를 찾았음. 이걸 이용)
            else if(sameCheck2(R.drawable.select2)){

                //상황파악(quantum으로 이동시 잡거나 업거나 그냥 놓이거나 확인)
                idnview = resources.getIdentifier("imageButton" + idn, "id", packageName)
                idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                //이동할 위치에 있는 말에 따라 이동
                subcm1()

            }

        }

    }

    //player2가 quantum으로 말 이동 선택하는 경우
    fun quantummove2(){
        (thisview as CustomImageButton).apply{

            //두개 패가 다르면 quantum move. 옮길 말 선택하면 그 말이 갈수있는 곳 표시
            if(sameCheck2(R.drawable.select) && selview_bgi == ""){
                delsel()
                setImageResource(R.drawable.select)
                selview = thisview
                svn = (selview as ImageButton)!!.context.resources.getResourceEntryName((selview as ImageButton)!!.id).substring(11).toInt()

                //옮길 말이 선택되면 그 말이 갈수 있는 곳 표시
                if (sameCheck(R.drawable.cr1)) {
                    selview_bgi = "cr"+1
                } else if (sameCheck(R.drawable.cr2)) {
                    selview_bgi = "cr"+2
                } else if (sameCheck(R.drawable.cr12)) {
                    selview_bgi = "cr"+12
                }

                showselect6()

            }
            //선택했던 말 다시 선택하면 선택취소
            else if(sameCheck2(R.drawable.select) && selview_bgi != ""){
                selview_bgi = ""
                delsel()
                showselect4()
            }

            //이동할 곳 선택 후 처리(showselect5 에서 이동할 수 있는 위치 두곳 idn 과 idn_2 를 찾았음. 이걸 이용)
            else if(sameCheck2(R.drawable.select2)){

                //상황파악(quantum으로 이동시 잡거나 업거나 그냥 놓이거나 확인)
                idnview = resources.getIdentifier("imageButton" + idn, "id", packageName)
                idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)

                //이동할 위치에 있는 말에 따라 이동
                subcm2()

            }

        }
    }


    //player1이 최종 말이 이동하기로 선택했을떄 주변상황 정리 : 선택표시창 지우고, 원래 위치의 말 지우고, 선택과 관련한 변수들 초기화
    fun final(){
        // 선택표시창 지우기
        delsel()
        // 원래 위치의 말 지우기
        if (svn == 29 || svn == 30 || svn == 31 || svn == 32) {
            (selview as ImageButton)!!.setBackgroundResource(android.R.color.transparent)
        }
        else{
            (selview as ImageButton)!!.setBackgroundResource(R.drawable.base)
            if(svn1 != 0){
                findViewById<View>(resources.getIdentifier("imageButton"+svn1, "id", packageName)).setBackgroundResource(R.drawable.base)
            }
        }

        //선택패 텍스트와 관련된 자료 초기화
        if(n == 1){
            text1 = ""
            if(sel == 1){
                t1_1.text = ""
                t1_1.setBackgroundResource(android.R.color.transparent)
                sel = 0
                svn1 = 0
            }
            else if(sel == 2){
                t1_2.text = ""
                t1_2.setBackgroundResource(android.R.color.transparent)
                sel = 0
                svn1 = 0
            }
            else if(sel == 3){
                t1_3.text = ""
                t1_3.setBackgroundResource(android.R.color.transparent)
                sel = 0
                svn1 = 0
            }
            else if(sel == 4){
                t1_4.text = ""
                t1_4.setBackgroundResource(android.R.color.transparent)
                sel = 0
                svn1 = 0
            }
        }
        else if(n == 2){

            t1_1.text = ""
            t1_2.text = ""
            t1_1.setBackgroundResource(android.R.color.transparent)
            t1_2.setBackgroundResource(android.R.color.transparent)
        }
    }
    //player2가 최종 말이 이동하기로 선택했을떄 주변상황 정리 : 선택표시창 지우고, 원래 위치의 말 지우고, 선택과 관련한 변수들 초기화
    fun final2(){
        // 선택표시창 지우기
        delsel()
        // 원래 위치의 말 지우기
        if (svn == 33 || svn == 34 || svn == 35 || svn == 36) {
            (selview as ImageButton)!!.setBackgroundResource(android.R.color.transparent)
        }
        else{
            (selview as ImageButton)!!.setBackgroundResource(R.drawable.base)
            if(svn1 != 0){
                findViewById<View>(resources.getIdentifier("imageButton"+svn1, "id", packageName)).setBackgroundResource(R.drawable.base)
            }
        }

        //선택패 텍스트와 관련된 자료 초기화
        if(m == 1){
            text1 = ""
            if(sel == 1){
                t2_1.text = ""
                t2_1.setBackgroundResource(android.R.color.transparent)
                sel = 0
                svn1 = 0
            }
            else if(sel == 2){
                t2_2.text = ""
                t2_2.setBackgroundResource(android.R.color.transparent)
                sel = 0
                svn1 = 0
            }
            else if(sel == 3){
                t2_3.text = ""
                t2_3.setBackgroundResource(android.R.color.transparent)
                sel = 0
                svn1 = 0
            }
            else if(sel == 4){
                t2_4.text = ""
                t2_4.setBackgroundResource(android.R.color.transparent)
                sel = 0
                svn1 = 0
            }
        }
        else if(m == 2){

            t2_1.text = ""
            t2_2.text = ""
            t2_1.setBackgroundResource(android.R.color.transparent)
            t2_2.setBackgroundResource(android.R.color.transparent)
        }
    }

    //text 선택하고 이동할 말 선택
    fun showselect3(){

        for(i in 0..28){

            val btnId = resources.getIdentifier("imageButton"+i, "id", packageName)
            (findViewById<View>(btnId) as CustomImageButton).apply {
                if (sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2) ||sameCheck(R.drawable.cb12)) {

                    setImageResource(R.drawable.select)
                }

                if((imageButton29 as CustomImageButton).sameCheck(R.drawable.cb1)){
                    (imageButton29 as CustomImageButton).setImageResource(R.drawable.select)
                }
                else if((imageButton30 as CustomImageButton).sameCheck(R.drawable.cb2)){
                    (imageButton30 as CustomImageButton).setImageResource(R.drawable.select)
                }

                //classic 인 경우엔 quantum 도 이동가능하므로 추가해주기
                if(n==1){
                    if(sameCheck(R.drawable.qb1)||sameCheck(R.drawable.qb1y)||sameCheck(R.drawable.qb1p)
                            ||sameCheck(R.drawable.qb2)||sameCheck(R.drawable.qb2y)||sameCheck(R.drawable.qb2p)
                            ||sameCheck(R.drawable.qb12y)||sameCheck(R.drawable.qb12p)
                            ||sameCheck(R.drawable.cb1qb2y)||sameCheck(R.drawable.cb1qb2p)
                            ||sameCheck(R.drawable.cb2qb1y)||sameCheck(R.drawable.cb2qb1p)){

                        setImageResource(R.drawable.select)
                    }
                }
                else if(n==2){
                    t1_1.setBackgroundResource(R.color.bgi_orange)
                    t1_2.setBackgroundResource(R.color.bgi_orange)
                    //일단 나온 패 확인
                    qt1_1 = t1_1.text.toString()
                    qt1_2 = t1_2.text.toString()

                    if (qt1_1 == "도") {
                        s1 = 1
                    } else if (qt1_1 == "개") {
                        s1 = 2
                    } else if (qt1_1 == "걸") {
                        s1 = 3
                    } else if (qt1_1 == "윷") {
                        s1 = 4
                    } else if (qt1_1 == "모") {
                        s1 = 5
                    } else if (qt1_1 == "빽도") {
                        s1 = 6
                    }
                    if (qt1_2 == "도") {
                        s2 = 1
                    } else if (qt1_2 == "개") {
                        s2 = 2
                    } else if (qt1_2 == "걸") {
                        s2 = 3
                    } else if (qt1_2 == "윷") {
                        s2 = 4
                    } else if (qt1_2 == "모") {
                        s2 = 5
                    } else if (qt1_2 == "빽도") {
                        s2 = 6
                    }

                    if(s1==s2){

                        n=1
                        s2=0
                        sel = 1
                        t1_2.text = ""
                        qt1_2 = ""
                        t1_2.setBackgroundResource(android.R.color.transparent)

                    }

                }
            }

        }

    }

    fun showselect4(){

        for(i in 0..28){

            val btnId = resources.getIdentifier("imageButton"+i, "id", packageName)
            (findViewById<View>(btnId) as CustomImageButton).apply {
                if (sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2) ||sameCheck(R.drawable.cr12)) {

                    setImageResource(R.drawable.select)
                }

                if((imageButton33 as CustomImageButton).sameCheck(R.drawable.cr1)){
                    (imageButton33 as CustomImageButton).setImageResource(R.drawable.select)
                }
                else if((imageButton34 as CustomImageButton).sameCheck(R.drawable.cr2)){
                    (imageButton34 as CustomImageButton).setImageResource(R.drawable.select)
                }


                //classic 인 경우엔 quantum 도 이동가능하므로 추가해주기
                if(m==1){
                    if(sameCheck(R.drawable.qr1)||sameCheck(R.drawable.qr1y)||sameCheck(R.drawable.qr1p)
                            ||sameCheck(R.drawable.qr2)||sameCheck(R.drawable.qr2y)||sameCheck(R.drawable.qr2p)
                            ||sameCheck(R.drawable.qr12y)||sameCheck(R.drawable.qr12p)
                            ||sameCheck(R.drawable.cr1qr2y)||sameCheck(R.drawable.cr1qr2p)
                            ||sameCheck(R.drawable.cr2qr1y)||sameCheck(R.drawable.cr2qr1p)){

                        setImageResource(R.drawable.select)
                    }
                }
                else if(m==2){
                    t2_1.setBackgroundResource(R.color.bgi_orange)
                    t2_2.setBackgroundResource(R.color.bgi_orange)
                    //일단 나온 패 확인
                    qt1_1 = t2_1.text.toString()
                    qt1_2 = t2_2.text.toString()

                    if (qt1_1 == "도") {
                        d1 = 1
                    } else if (qt1_1 == "개") {
                        d1 = 2
                    } else if (qt1_1 == "걸") {
                        d1 = 3
                    } else if (qt1_1 == "윷") {
                        d1 = 4
                    } else if (qt1_1 == "모") {
                        d1 = 5
                    } else if (qt1_1 == "빽도") {
                        d1 = 6
                    }
                    if (qt1_2 == "도") {
                        d2 = 1
                    } else if (qt1_2 == "개") {
                        d2 = 2
                    } else if (qt1_2 == "걸") {
                        d2 = 3
                    } else if (qt1_2 == "윷") {
                        d2 = 4
                    } else if (qt1_2 == "모") {
                        d2 = 5
                    } else if (qt1_2 == "빽도") {
                        d2 = 6
                    }

                    if(d1==d2){

                        m=1
                        d2=0
                        sel = 1
                        t2_2.text = ""
                        qt1_2 = ""
                        t2_2.setBackgroundResource(android.R.color.transparent)

                    }

                }
            }

        }

    }

    //player1이 이동하고자 하는 말 선택 후 그 말이 이동가능한 위치( classic 일때는 idn과 지름길 idn_2 표시. quantum 일때는 두 위치 idn과 idn_2 표시 )
    fun showselect5(){

        if(n==1){

            delsel()
            (selview as ImageButton).setImageResource(R.drawable.select)

            // 이동하려고 선택한 말이 quantum 말이면
            if(selview_bgi.substring(0,1) == "q"||selview_bgi.substring(selview_bgi.length-1) == "y"
                    || selview_bgi.substring(selview_bgi.length-1) == "p"){
                for(i in 0..28){
                    if(i != svn && selview_bgi == "qb1"){ //이동하려고 선택한 말이 qb1일때 다른 위치에 있는 qb1 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1)) { //i는 svn과 다른 위치에 있으면서 qb1인 말 번호

                            svn1 = i
                            //svn과 svn1이 갈 수 있는 곳 표시
                            subss5()

                        }
                    }
                    else if(i != svn && selview_bgi == "qb2"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2)) { //i는 svn과 다른 위치에 있으면서 qb2인 말 번호

                            svn1 = i
                            subss5()

                        }
                    }
                    else if(i != svn && selview_bgi == "qb1y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12p)) {

                            svn1 = i

                        }
                        if(svn1 != 0){
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2y)) {

                                svn2 = i
                                subss5()

                            }
                        }
                        else if(svn1 == 0){ //qb1y는 있는데 qb12p가 없다는 것은 cb2qb1p 가 있다는 것
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1p)) {

                                svn1 = i
                                subss5()

                            }

                        }
                    }
                    else if(i != svn && selview_bgi == "qb1p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) {

                            svn1 = i

                        }
                        if(svn1 != 0){
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) {

                                svn2 = i
                                subss5()

                            }
                        }
                        else if(svn1 == 0){ //qb1y는 있는데 qb12p가 없다는 것은 cb2qb1p 가 있다는 것
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb2qb1y)) {

                                svn1 = i
                                subss5()

                            }

                        }
                    }
                    else if(i != svn && selview_bgi == "qb2y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12p)) {

                            svn1 = i

                        }
                        if(svn1 != 0){
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1y)) {

                                svn2 = i
                                subss5()

                            }
                        }
                        else if(svn1 == 0){ //qb1y는 있는데 qb12p가 없다는 것은 cb2qb1p 가 있다는 것
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2p)) {

                                svn1 = i
                                subss5()

                            }

                        }
                    }
                    else if(i != svn && selview_bgi == "qb2p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) {

                            svn1 = i

                        }
                        if(svn1 != 0){
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) {

                                svn2 = i
                                subss5()

                            }
                        }
                        else if(svn1 == 0){ //qb1y는 있는데 qb12p가 없다는 것은 cb2qb1p 가 있다는 것
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cb1qb2y)) {

                                svn1 = i
                                subss5()

                            }

                        }
                    }
                    else if(i != svn && selview_bgi == "qb12y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        svn1 = 0
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12p)) {

                            svn1 = i
                            subss5()

                        }
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) {

                            svn1 = i

                        }
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) {

                            svn2 = i
                            subss5()

                        }

                    }
                    else if(i != svn && selview_bgi == "qb12p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb12y)) {

                            svn1 = i
                            subss5()

                        }
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1y)) {

                            svn1 = i

                        }
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2y)) {

                            svn2 = i
                            subss5()

                        }

                    }
                    else if(i != svn && selview_bgi == "cb1qb2y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2p)) {

                            svn1 = i
                            subss5()

                        }

                    }
                    else if(i != svn && selview_bgi == "cb1qb2p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb2y)) {

                            svn1 = i
                            subss5()

                        }

                    }
                    else if(i != svn && selview_bgi == "cb2qb1y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1p)) {

                            svn1 = i
                            subss5()

                        }

                    }
                    else if(i != svn && selview_bgi == "cb2qb1p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qb1y)) {

                            svn1 = i
                            subss5()

                        }

                    }

                }
            }
            // classic말을 classic으로 옮길때
            else{
                idn = svn + s1
                idn_2 = 100
                //보통의 경우와 뒤에있는애는 직진
                if (svn in 29..32) {
                    if(s1 == 6){idn = 28}
                    else{idn = s1}
                }
                else if (svn in 1..19) {
                    idn = svn + s1
                    if(s1 == 6){idn = svn -1}
                    if(idn >= 20){idn = 0}

                    //원래 이동하려는 말이 갈림길에 있었으면 지름길로 갈 수 있는 위치 idn_2 도 추가로 알려줌
                    if(svn == 5 && s1 !=6){
                        idn_2 = idn + 14
                    }
                    else if(svn == 10 && s1 !=6){
                        if(idn in 11..12){idn_2 = idn+14}
                        else if(idn == 13){idn_2 = 22}
                        else if(idn in 14..15){idn_2 = idn + 13}

                    }

                }
                else if (svn in 20..24) {
                    if(s1 == 6){idn = svn -1}
                    if(idn == 19){idn = 5}
                    if(idn in 25..29){idn = idn -10}

                    //원래 이동하려는 말이 22에 있었으면 지름길로 갈 수 있는 위치 idn_2 도 추가로 알려줌
                    if(svn == 22 && s1 !=6){
                        if(idn in 23..24){idn_2 = idn +4}
                        else if(idn in 15..17){idn_2 = 0}
                    }
                }
                else if(svn in 25..28){
                    if(s1 == 6){idn = svn - 1}

                    if(svn in 25..26){
                        if(idn == 24){idn = 10}
                        else if(idn == 27){idn = 22}
                        else if(idn in 28..29){idn = idn -1}
                        else if(idn in 30..33){idn = 0}
                    }
                    else if(svn in 27..28){
                        if(idn == 26){idn = 22}
                        else if(idn in 29..33){idn = 0}
                    }
                }
                else if(svn == 0){idn = 0}

                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                //지름길로 가는 경우가 있으면 idn_2도 표현해주기
                if(idn_2 != 100){
                    (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                }

            }

        }

        else if(n==2){
            if (svn in 29..32) {
                if(s1 == 6){s1 = 28} else if(s2 == 6){s2 = 28}
                idn = s1
                idn_2 = s2
                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
            }

            else if (svn in 1..19) {
                if(s1 == 6){
                    idn = svn -1
                    idn_2 = svn + s2
                } else if(s2 == 6){
                    idn_2 = svn -1
                    idn = svn + s1
                } else if(s1 != 6 && s2 != 6 && s1>=s2){ // 짧은게 직진할수있게 idn 은 짧은 s2 더한것으로 설정
                    idn_2 = svn + s1
                    idn = svn + s2
                } else if(s1 != 6 && s2 != 6 && s1<=s2){ // 짧은게 직진할수있게 idn 은 짧은 s1 더한것으로 설정
                    idn = svn + s1
                    idn_2 = svn + s2
                }
                if(idn >= 20){idn = 0}
                if(idn_2 >= 20){idn_2 = 0}


                //idn을 직진하고 idn_2를 지름길로 표시하는 방법
                if(svn in 1..4){
                    if(idn_2 in 1..5){
                    }
                    else if(idn_2 in 6..9){
                        idn_2 = idn_2+14
                    }
                    else if(idn_2 == 10){
                        idn_2 = 15
                    }

                }
                else if(svn == 5){
                    if(idn in 6..8){
                        idn = idn+14

                    }
                    else if(idn in 9..10){
                        idn = idn + 18
                    }

                    if(idn_2 in 6..8){
                        idn_2 = idn_2 + 14
                    }
                    else if(idn_2 in 9..10){
                        idn_2 = idn_2 + 18
                    }
                }
                else if(svn in 6..9){
                    if(idn_2 in 11..15){
                        if(idn_2 in 11..12){idn_2 = idn_2+14}
                        else if(idn_2 == 13){idn_2 = 22}
                        else if(idn_2 in 14..15){idn_2 = idn_2+13}

                    }
                }
                else if(svn == 10){
                    if(idn in 11..12){
                        idn = idn+14

                    }
                    else if(idn == 13){
                        idn = 22
                    }
                    else if(idn in 14..15){
                        idn = idn + 13
                    }

                    if(idn_2 in 11..12){
                        idn_2 = idn_2 +14

                    }
                    else if(idn_2 == 13){
                        idn_2 = 22
                    }
                    else if(idn_2 in 14..15){
                        idn_2 = idn_2 + 13
                    }
                }

                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)



            }

            else if (svn in 20..24) { //idn이 짧게 움직이는거 idn_2가 길게움직이는거
                if(s1 == 6){
                    idn = svn -1
                    idn_2 = svn + s2
                } else if(s2 == 6){
                    idn_2 = svn + s1
                    idn = svn - 1
                } else if(s1 != 6 && s2 != 6 && s1>=s2){ // 짧은게 직진할수있게 idn 은 짧은 s2 더한것으로 설정
                    idn_2 = svn + s1
                    idn = svn + s2
                } else if(s1 != 6 && s2 != 6 && s1<=s2){ // 짧은게 직진할수있게 idn 은 짧은 s1 더한것으로 설정
                    idn = svn + s1
                    idn_2 = svn + s2
                }

                if(svn in 20..21){
                    if(idn in 25..29){
                        idn = idn - 10
                    }
                    else if(idn == 19){
                        idn = 5
                    }
                    if(idn_2 in 23..24){
                        idn_2 = idn_2 + 4
                    }
                    else if(idn_2 in 25..29){
                        idn_2 = 0
                    }

                }
                else if(svn == 22){
                    if(idn in 23..24){
                        idn = idn + 4
                    }
                    else if(idn in 25..29){
                        idn = 0
                    }

                    if(idn_2 in 23..24){
                        idn_2 = idn_2 + 4
                    }
                    else if(idn_2 in 25..29){
                        idn_2 = 0
                    }

                }
                else if(svn in 23..24){
                    if(idn in 25..29){
                        idn = idn - 10
                    }
                    if(idn_2 in 25..29){
                        idn_2 = idn_2 - 10
                    }
                }


                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)


            }

            else if(svn in 25..28){
                if(s1 == 6){
                    idn = svn -1
                    idn_2 = svn + s2
                } else if(s2 == 6){
                    idn_2 = svn + s1
                    idn = svn - 1
                } else if(s1 != 6 && s2 != 6 && s1>=s2){ // 짧은게 직진할수있게 idn 은 짧은 s2 더한것으로 설정
                    idn_2 = svn + s1
                    idn = svn + s2
                } else if(s1 != 6 && s2 != 6 && s1<=s2){ // 짧은게 직진할수있게 idn 은 짧은 s1 더한것으로 설정
                    idn = svn + s1
                    idn_2 = svn + s2
                }

                if(idn == 24){
                    idn = 10
                }
                else if(svn in 25..26){
                    if(idn == 27){
                        idn = 22
                    }
                    else if(idn in 28..29){
                        idn = idn -1
                    }
                    else if(idn in 30..33){
                        idn = 0
                    }

                    if(idn_2 == 27){
                        idn_2 = 22
                    }
                    else if(idn_2 in 28..29){
                        idn_2 = idn_2 -1
                    }
                    else if(idn_2 in 30..33){
                        idn_2 = 0
                    }
                }
                else if(svn in 27..28){
                    if(idn == 26){
                        idn = 22
                    }
                    else if(idn in 29..33){
                        idn = 0
                    }

                    if(idn_2 == 26){
                        idn_2 = 22
                    }
                    else if(idn_2 in 29..33){
                        idn_2 = 0
                    }
                }

                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
            }

            else if(svn == 0){
                (findViewById<View>(resources.getIdentifier("imageButton" + 0, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
            }
        }

    }
    //showselect5() 의 보조함수. quantum 말을 classic으로 옮길때 위차파악
    fun subss5(){

        idn = svn + s1 // 선택한 말이 이동할 위치
        idn_2 = svn1 + s1 // 선택한 말의 다른 쌍 말이 이동할 위치
        if(svn2 in 1..28){
            idn2 = svn2 + s1 // 선택한 말의 다른 쌍이 또 있을떄
        }


        //idn이 갈림길에서 꺾임
        if (svn in 1..19) {
            if(s1 == 6){idn = svn -1}
            if(idn >= 20){idn = 0}

            //idn이 중간에 갈림길을 만나면 꺾임
            if(svn in 1..5){

                if(idn in 6..8){ idn = idn+14 }
                else if(idn in 9..10){ idn = idn + 18 }
            }
            else if(svn in 6..10){

                if(idn in 11..12){idn = idn +14}
                else if(idn == 13){idn = 22}
                else if(idn in 14..15){idn = idn+13}
            }
        } else if (svn in 20..24) {
            if(s1 == 6){idn = svn -1}
            if(idn == 19){idn = 5}

            //22에서 갈림길을 만나면 어떻게 할 것인지에 대한 처리
            if(svn in 20..22){
                //22를 지나는 경우 지름길로 움직임
                if(idn in 23..24){ idn = idn + 4 }
                else if(idn in 25..27){idn = 0}
            }
            else if(svn in 23..24){
                if(idn in 25..29){idn = idn - 10}
            }
        } else if(svn in 25..28){
            if(s1 == 6){idn = svn -1}
            if(svn in 25..26){
                if(idn == 24){idn = 10}
                else if(idn == 27){idn = 22}
                else if(idn in 28..29){idn = idn -1}
                else if(idn in 30..33){idn = 0}
            }
            else if(svn in 27..28){
                if(idn == 26){idn = 22}
                else if(idn in 29..33){idn = 0}
            }

        }

        //idn_2이 갈림길에서 꺾임
        if (svn1 in 1..19) {
            if(s1 == 6){idn_2 = svn1 -1}
            if(idn_2 >= 20){idn_2 = 0}

            //idn이 중간에 갈림길을 만나면 꺾임
            if(svn1 in 1..5){

                if(idn_2 in 6..8){ idn_2 = idn_2 +14 }
                else if(idn_2 in 9..10){ idn_2 = idn_2 + 18 }
            }
            else if(svn1 in 6..10){

                if(idn_2 in 11..12){idn_2 = idn_2 + 14}
                else if(idn_2 == 13){idn_2 = 22}
                else if(idn_2 in 14..15){idn_2 = idn_2 + 13}
            }

        } else if (svn1 in 20..24) {
            if(s1 == 6){idn_2 = svn1 -1}
            if(idn_2 == 19){idn_2 = 5}

            //22에서 갈림길을 만나면 어떻게 할 것인지에 대한 처리
            if(svn1 in 20..22){
                //22를 지나는 경우 지름길로 움직임
                if(idn_2 in 23..24){ idn_2 = idn_2 + 4}
                else if(idn_2 in 25..27){idn_2 = 0}
            }
            else if(svn1 in 23..24){
                if(idn_2 in 25..29){idn_2 = idn_2 - 10}
            }

        } else if(svn1 in 25..28){
            if(s1 == 6){idn_2 = svn1 -1}
            if(svn1 in 25..26){
                if(idn_2 == 24){idn_2 = 10}
                else if(idn_2 == 27){idn_2 = 22}
                else if(idn_2 in 28..29){idn_2 = idn_2 -1}
                else if(idn_2 in 30..33){idn_2 = 0}
            }
            else if(svn1 in 27..28){
                if(idn_2 == 26){idn_2 = 22}
                else if(idn_2 in 29..33){idn_2 = 0}
            }

        }
        
        if(svn2 in 1..28){
            //idn2이 갈림길에서 꺾임
            if (svn2 in 1..19) {
                if(s1 == 6){idn2 = svn2 -1}
                if(idn2 >= 20){idn2 = 0}

                //idn이 중간에 갈림길을 만나면 꺾임
                if(svn2 in 1..5){

                    if(idn2 in 6..8){ idn2 = idn2 +14 }
                    else if(idn2 in 9..10){ idn2 = idn2 + 18 }
                }
                else if(svn2 in 6..10){

                    if(idn2 in 11..12){idn2 = idn2 + 14}
                    else if(idn2 == 13){idn2 = 22}
                    else if(idn2 in 14..15){idn2 = idn2 + 13}
                }

            } else if (svn2 in 20..24) {
                if(s1 == 6){idn2 = svn2 -1}
                if(idn2 == 19){idn2 = 5}

                //22에서 갈림길을 만나면 어떻게 할 것인지에 대한 처리
                if(svn2 in 20..22){
                    //22를 지나는 경우 지름길로 움직임
                    if(idn2 in 23..24){ idn2 = idn2 + 4}
                    else if(idn2 in 25..27){idn2 = 0}
                }
                else if(svn2 in 23..24){
                    if(idn2 in 25..29){idn2 = idn2 - 10}
                }

            } else if(svn2 in 25..28){
                if(s1 == 6){idn2 = svn2 -1}
                if(svn2 in 25..26){
                    if(idn2 == 24){idn2 = 10}
                    else if(idn2 == 27){idn2 = 22}
                    else if(idn2 in 28..29){idn2 = idn2 -1}
                    else if(idn2 in 30..33){idn2 = 0}
                }
                else if(svn2 in 27..28){
                    if(idn2 == 26){idn2 = 22}
                    else if(idn2 in 29..33){idn2 = 0}
                }

            }
        }

        idnview = resources.getIdentifier("imageButton" + idn, "id", packageName)
        idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
        (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
        (findViewById<View>(idn_2view) as CustomImageButton).setImageResource(R.drawable.select2)
        if(svn2 in 1..28){
            idn2view = resources.getIdentifier("imageButton" + idn2, "id", packageName)
            (findViewById<View>(idn2view) as CustomImageButton).setImageResource(R.drawable.select2)
        }
    }

    //player2가 이동하고자 하는 말 선택 후 그 말이 이동가능한 위치
    fun showselect6(){

        if(m==1){

            delsel()
            (selview as ImageButton).setImageResource(R.drawable.select)

            // 이동하려고 선택한 말이 quantum 말이면
            if(selview_bgi.substring(0,1) == "q"||selview_bgi.substring(selview_bgi.length-1) == "y"
                    || selview_bgi.substring(selview_bgi.length-1) == "p"){
                for(i in 0..28){
                    if(i != svn && selview_bgi == "qr1"){ //이동하려고 선택한 말이 qb1일때 다른 위치에 있는 qb1 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1)) { //i는 svn과 다른 위치에 있으면서 qb1인 말 번호

                            svn1 = i
                            //svn과 svn1이 갈 수 있는 곳 표시
                            subss6()

                        }
                    }
                    else if(i != svn && selview_bgi == "qr2"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2)) { //i는 svn과 다른 위치에 있으면서 qb2인 말 번호

                            svn1 = i
                            subss6()

                        }
                    }
                    else if(i != svn && selview_bgi == "qr1y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12p)) {

                            svn1 = i

                        }
                        if(svn1 != 0){
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2y)) {

                                svn2 = i
                                subss6()

                            }
                        }
                        else if(svn1 == 0){ //qb1y는 있는데 qb12p가 없다는 것은 cb2qb1p 가 있다는 것
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1p)) {

                                svn1 = i
                                subss6()

                            }

                        }
                    }
                    else if(i != svn && selview_bgi == "qr1p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) {

                            svn1 = i

                        }
                        if(svn1 != 0){
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) {

                                svn2 = i
                                subss6()

                            }
                        }
                        else if(svn1 == 0){ //qb1y는 있는데 qb12p가 없다는 것은 cb2qb1p 가 있다는 것
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr2qr1y)) {

                                svn1 = i
                                subss6()

                            }

                        }
                    }
                    else if(i != svn && selview_bgi == "qr2y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12p)) {

                            svn1 = i

                        }
                        if(svn1 != 0){
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1y)) {

                                svn2 = i
                                subss6()

                            }
                        }
                        else if(svn1 == 0){ //qb1y는 있는데 qb12p가 없다는 것은 cb2qb1p 가 있다는 것
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2p)) {

                                svn1 = i
                                subss6()

                            }

                        }
                    }
                    else if(i != svn && selview_bgi == "qr2p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) {

                            svn1 = i

                        }
                        if(svn1 != 0){
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) {

                                svn2 = i
                                subss6()

                            }
                        }
                        else if(svn1 == 0){ //qb1y는 있는데 qb12p가 없다는 것은 cb2qb1p 가 있다는 것
                            if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.cr1qr2y)) {

                                svn1 = i
                                subss6()

                            }

                        }
                    }
                    else if(i != svn && selview_bgi == "qr12y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        svn1 = 0
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12p)) {

                            svn1 = i
                            subss6()

                        }
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) {

                            svn1 = i

                        }
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) {

                            svn2 = i
                            subss6()

                        }

                    }
                    else if(i != svn && selview_bgi == "qr12p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr12y)) {

                            svn1 = i
                            subss6()

                        }
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1y)) {

                            svn1 = i

                        }
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2y)) {

                            svn2 = i
                            subss6()

                        }

                    }
                    else if(i != svn && selview_bgi == "cr1qr2y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2p)) {

                            svn1 = i
                            subss6()

                        }

                    }
                    else if(i != svn && selview_bgi == "cr1qr2p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr2y)) {

                            svn1 = i
                            subss6()

                        }

                    }
                    else if(i != svn && selview_bgi == "cr2qr1y"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1p)) {

                            svn1 = i
                            subss6()

                        }

                    }
                    else if(i != svn && selview_bgi == "cr2qr1p"){ //이동하려고 선택한 말이 qb2일때 다른 위치에 있는 qb2 찾기
                        val btnid = resources.getIdentifier("imageButton" + i, "id", packageName)
                        if((findViewById<View>(btnid) as CustomImageButton).sameCheck(R.drawable.qr1y)) {

                            svn1 = i
                            subss6()

                        }

                    }

                }
            }
            // classic말을 classic으로 옮길때
            else{
                idn = svn + d1
                idn_2 = 100
                //보통의 경우와 뒤에있는애는 직진
                if (svn in 33..36) {
                    if(d1 == 6){idn = 28}
                    else{idn = d1}
                }
                else if (svn in 1..19) {
                    idn = svn + d1
                    if(d1 == 6){idn = svn -1}
                    if(idn >= 20){idn = 0}

                    //원래 이동하려는 말이 갈림길에 있었으면 지름길로 갈 수 있는 위치 idn_2 도 추가로 알려줌
                    if(svn == 5 && d1 !=6){
                        idn_2 = idn + 14
                    }
                    else if(svn == 10 && d1 !=6){
                        if(idn in 11..12){idn_2 = idn+14}
                        else if(idn == 13){idn_2 = 22}
                        else if(idn in 14..15){idn_2 = idn + 13}

                    }

                }
                else if (svn in 20..24) {
                    if(d1 == 6){idn = svn -1}
                    if(idn == 19){idn = 5}
                    if(idn in 25..29){idn = idn -10}

                    //원래 이동하려는 말이 22에 있었으면 지름길로 갈 수 있는 위치 idn_2 도 추가로 알려줌
                    if(svn == 22 && d1 !=6){
                        if(idn in 23..24){idn_2 = idn +4}
                        else if(idn in 15..17){idn_2 = 0}
                    }
                }
                else if(svn in 25..28){
                    if(d1 == 6){idn = svn - 1}

                    if(svn in 25..26){
                        if(idn == 24){idn = 10}
                        else if(idn == 27){idn = 22}
                        else if(idn in 28..29){idn = idn -1}
                        else if(idn in 30..33){idn = 0}
                    }
                    else if(svn in 27..28){
                        if(idn == 26){idn = 22}
                        else if(idn in 29..33){idn = 0}
                    }
                }
                else if(svn == 0){idn = 0}

                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                //지름길로 가는 경우가 있으면 idn_2도 표현해주기
                if(idn_2 != 100){
                    (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                }

            }

        }

        else if(m==2){
            if (svn in 33..36) {
                if(d1 == 6){d1 = 28} else if(d2 == 6){d2 = 28}
                idn = d1
                idn_2 = d2
                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
            }

            else if (svn in 1..19) {
                if(d1 == 6){
                    idn = svn -1
                    idn_2 = svn + d2
                } else if(d2 == 6){
                    idn_2 = svn -1
                    idn = svn + d1
                } else if(d1 != 6 && d2 != 6 && d1>=d2){ // 짧은게 직진할수있게 idn 은 짧은 s2 더한것으로 설정
                    idn_2 = svn + d1
                    idn = svn + d2
                } else if(d1 != 6 && d2 != 6 && d1<=d2){ // 짧은게 직진할수있게 idn 은 짧은 s1 더한것으로 설정
                    idn = svn + d1
                    idn_2 = svn + d2
                }
                if(idn >= 20){idn = 0}
                if(idn_2 >= 20){idn_2 = 0}


                //idn을 직진하고 idn_2를 지름길로 표시하는 방법
                if(svn in 1..4){
                    if(idn_2 in 1..5){
                    }
                    else if(idn_2 in 6..9){
                        idn_2 = idn_2+14
                    }
                    else if(idn_2 == 10){
                        idn_2 = 15
                    }

                }
                else if(svn == 5){
                    if(idn in 6..8){
                        idn = idn+14

                    }
                    else if(idn in 9..10){
                        idn = idn + 18
                    }

                    if(idn_2 in 6..8){
                        idn_2 = idn_2 + 14
                    }
                    else if(idn_2 in 9..10){
                        idn_2 = idn_2 + 18
                    }
                }
                else if(svn in 6..9){
                    if(idn_2 in 11..15){
                        if(idn_2 in 11..12){idn_2 = idn_2+14}
                        else if(idn_2 == 13){idn_2 = 22}
                        else if(idn_2 in 14..15){idn_2 = idn_2+13}

                    }
                }
                else if(svn == 10){
                    if(idn in 11..12){
                        idn = idn+14

                    }
                    else if(idn == 13){
                        idn = 22
                    }
                    else if(idn in 14..15){
                        idn = idn + 13
                    }

                    if(idn_2 in 11..12){
                        idn_2 = idn_2 +14

                    }
                    else if(idn_2 == 13){
                        idn_2 = 22
                    }
                    else if(idn_2 in 14..15){
                        idn_2 = idn_2 + 13
                    }
                }

                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)



            }

            else if (svn in 20..24) { //idn이 짧게 움직이는거 idn_2가 길게움직이는거
                if(d1 == 6){
                    idn = svn -1
                    idn_2 = svn + d2
                } else if(d2 == 6){
                    idn_2 = svn + d1
                    idn = svn - 1
                } else if(d1 != 6 && d2 != 6 && d1>=d2){ // 짧은게 직진할수있게 idn 은 짧은 s2 더한것으로 설정
                    idn_2 = svn + d1
                    idn = svn + d2
                } else if(d1 != 6 && d2 != 6 && d1<=d2){ // 짧은게 직진할수있게 idn 은 짧은 s1 더한것으로 설정
                    idn = svn + d1
                    idn_2 = svn + d2
                }

                if(svn in 20..21){
                    if(idn in 25..29){
                        idn = idn - 10
                    }
                    else if(idn == 19){
                        idn = 5
                    }
                    if(idn_2 in 23..24){
                        idn_2 = idn_2 + 4
                    }
                    else if(idn_2 in 25..29){
                        idn_2 = 0
                    }

                }
                else if(svn == 22){
                    if(idn in 23..24){
                        idn = idn + 4
                    }
                    else if(idn in 25..29){
                        idn = 0
                    }

                    if(idn_2 in 23..24){
                        idn_2 = idn_2 + 4
                    }
                    else if(idn_2 in 25..29){
                        idn_2 = 0
                    }

                }
                else if(svn in 23..24){
                    if(idn in 25..29){
                        idn = idn - 10
                    }
                    if(idn_2 in 25..29){
                        idn_2 = idn_2 - 10
                    }
                }


                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)


            }

            else if(svn in 25..28){
                if(d1 == 6){
                    idn = svn -1
                    idn_2 = svn + d2
                } else if(d2 == 6){
                    idn_2 = svn + d1
                    idn = svn - 1
                } else if(d1 != 6 && d2 != 6 && d1>=d2){ // 짧은게 직진할수있게 idn 은 짧은 s2 더한것으로 설정
                    idn_2 = svn + d1
                    idn = svn + d2
                } else if(d1 != 6 && d2 != 6 && d1<=d2){ // 짧은게 직진할수있게 idn 은 짧은 s1 더한것으로 설정
                    idn = svn + d1
                    idn_2 = svn + d2
                }

                if(idn == 24){
                    idn = 10
                }
                else if(svn in 25..26){
                    if(idn == 27){
                        idn = 22
                    }
                    else if(idn in 28..29){
                        idn = idn -1
                    }
                    else if(idn in 30..33){
                        idn = 0
                    }

                    if(idn_2 == 27){
                        idn_2 = 22
                    }
                    else if(idn_2 in 28..29){
                        idn_2 = idn_2 -1
                    }
                    else if(idn_2 in 30..33){
                        idn_2 = 0
                    }
                }
                else if(svn in 27..28){
                    if(idn == 26){
                        idn = 22
                    }
                    else if(idn in 29..33){
                        idn = 0
                    }

                    if(idn_2 == 26){
                        idn_2 = 22
                    }
                    else if(idn_2 in 29..33){
                        idn_2 = 0
                    }
                }

                (findViewById<View>(resources.getIdentifier("imageButton" + idn, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
                (findViewById<View>(resources.getIdentifier("imageButton" + idn_2, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
            }

            else if(svn == 0){
                (findViewById<View>(resources.getIdentifier("imageButton" + 0, "id", packageName)) as CustomImageButton).setImageResource(R.drawable.select2)
            }
        }

    }
    //showselect6() 의 보조함수. quantum 말을 classic으로 옮길때 위차파악
    fun subss6(){

        idn = svn + d1 // 선택한 말이 이동할 위치
        idn_2 = svn1 + d1 // 선택한 말의 다른 쌍 말이 이동할 위치
        if(svn2 in 1..28){
            idn2 = svn2 + d1
        }

        //idn이 갈림길에서 꺾임
        if (svn in 1..19) {
            if(d1 == 6){idn = svn -1}
            if(idn >= 20){idn = 0}

            //idn이 중간에 갈림길을 만나면 꺾임
            if(svn in 1..5){

                if(idn in 6..8){ idn = idn+14 }
                else if(idn in 9..10){ idn = idn + 18 }
            }
            else if(svn in 6..10){

                if(idn in 11..12){idn = idn +14}
                else if(idn == 13){idn = 22}
                else if(idn in 14..15){idn = idn+13}
            }
        } else if (svn in 20..24) {
            if(d1 == 6){idn = svn -1}
            if(idn == 19){idn = 5}

            //22에서 갈림길을 만나면 어떻게 할 것인지에 대한 처리
            if(svn in 20..22){
                //22를 지나는 경우 지름길로 움직임
                if(idn in 23..24){ idn = idn + 4 }
                else if(idn in 25..27){idn = 0}
            }
            else if(svn in 23..24){
                if(idn in 25..29){idn = idn - 10}
            }
        } else if(svn in 25..28){
            if(d1 == 6){idn = svn -1}
            if(svn in 25..26){
                if(idn == 24){idn = 10}
                else if(idn == 27){idn = 22}
                else if(idn in 28..29){idn = idn -1}
                else if(idn in 30..33){idn = 0}
            }
            else if(svn in 27..28){
                if(idn == 26){idn = 22}
                else if(idn in 29..33){idn = 0}
            }

        }

        //idn_2이 갈림길에서 꺾임
        if (svn1 in 1..19) {
            if(d1 == 6){idn_2 = svn1 -1}
            if(idn_2 >= 20){idn_2 = 0}

            //idn이 중간에 갈림길을 만나면 꺾임
            if(svn1 in 1..5){

                if(idn_2 in 6..8){ idn_2 = idn_2 +14 }
                else if(idn_2 in 9..10){ idn_2 = idn_2 + 18 }
            }
            else if(svn1 in 6..10){

                if(idn_2 in 11..12){idn_2 = idn_2 + 14}
                else if(idn_2 == 13){idn_2 = 22}
                else if(idn_2 in 14..15){idn_2 = idn_2 + 13}
            }

        } else if (svn1 in 20..24) {
            if(d1 == 6){idn_2 = svn1 -1}
            if(idn_2 == 19){idn_2 = 5}

            //22에서 갈림길을 만나면 어떻게 할 것인지에 대한 처리
            if(svn1 in 20..22){
                //22를 지나는 경우 지름길로 움직임
                if(idn_2 in 23..24){ idn_2 = idn_2 + 4}
                else if(idn_2 in 25..27){idn_2 = 0}
            }
            else if(svn1 in 23..24){
                if(idn_2 in 25..29){idn_2 = idn_2 - 10}
            }

        } else if(svn1 in 25..28){
            if(d1 == 6){idn_2 = svn1 -1}
            if(svn1 in 25..26){
                if(idn_2 == 24){idn_2 = 10}
                else if(idn_2 == 27){idn_2 = 22}
                else if(idn_2 in 28..29){idn_2 = idn_2 -1}
                else if(idn_2 in 30..33){idn_2 = 0}
            }
            else if(svn1 in 27..28){
                if(idn_2 == 26){idn_2 = 22}
                else if(idn_2 in 29..33){idn_2 = 0}
            }

        }

        if(svn2 in 1..28){
            if (svn2 in 1..19) {
                if(d1 == 6){idn_2 = svn2 -1}
                if(idn2 >= 20){idn2 = 0}

                //idn이 중간에 갈림길을 만나면 꺾임
                if(svn2 in 1..5){

                    if(idn2 in 6..8){ idn2 = idn2 +14 }
                    else if(idn2 in 9..10){ idn2 = idn2 + 18 }
                }
                else if(svn2 in 6..10){

                    if(idn2 in 11..12){idn2 = idn2 + 14}
                    else if(idn2 == 13){idn2 = 22}
                    else if(idn2 in 14..15){idn2 = idn2 + 13}
                }

            } else if (svn2 in 20..24) {
                if(d1 == 6){idn2 = svn2 -1}
                if(idn2 == 19){idn2 = 5}

                //22에서 갈림길을 만나면 어떻게 할 것인지에 대한 처리
                if(svn2 in 20..22){
                    //22를 지나는 경우 지름길로 움직임
                    if(idn2 in 23..24){ idn2 = idn2 + 4}
                    else if(idn2 in 25..27){idn2 = 0}
                }
                else if(svn2 in 23..24){
                    if(idn2 in 25..29){idn2 = idn2 - 10}
                }

            } else if(svn2 in 25..28){
                if(d1 == 6){idn2 = svn2 -1}
                if(svn2 in 25..26){
                    if(idn2 == 24){idn2 = 10}
                    else if(idn2 == 27){idn2 = 22}
                    else if(idn2 in 28..29){idn2 = idn2 -1}
                    else if(idn2 in 30..33){idn2 = 0}
                }
                else if(svn2 in 27..28){
                    if(idn2 == 26){idn2 = 22}
                    else if(idn2 in 29..33){idn2 = 0}
                }

            }
        }

        idnview = resources.getIdentifier("imageButton" + idn, "id", packageName)
        idn_2view = resources.getIdentifier("imageButton" + idn_2, "id", packageName)
        (findViewById<View>(idnview) as CustomImageButton).setImageResource(R.drawable.select2)
        (findViewById<View>(idn_2view) as CustomImageButton).setImageResource(R.drawable.select2)
        if(svn2 in 1..28){
            idn2view = resources.getIdentifier("imageButton" + idn2, "id", packageName)
            (findViewById<View>(idn2view) as CustomImageButton).setImageResource(R.drawable.select2)
        }
    }

    //quantum 으로 쪼개진 말은 어떻게 놓일까
    fun quantumpiece(){

        if(n == 2){
            //두 곳이 다 비어있을때
            if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.base)
                    && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.base)){
                if(selview_bgi == "cb"+1){
                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb1)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb1)
                } else if(selview_bgi == "cb"+2){
                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qb2)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qb2)
                }

            }

            //나머지 정리
            final()

        }
        else if(m==2){
            //두 곳이 다 비어있을때
            if((findViewById<View>(idnview) as CustomImageButton).sameCheck(R.drawable.base)
                    && (findViewById<View>(idn_2view) as CustomImageButton).sameCheck(R.drawable.base)){
                if(selview_bgi == "cr"+1){
                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr1)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr1)
                } else if(selview_bgi == "cr"+2){
                    findViewById<View>(idnview).setBackgroundResource(R.drawable.qr2)
                    findViewById<View>(idn_2view).setBackgroundResource(R.drawable.qr2)
                }

            }

            //나머지 정리
            final2()

        }


    }

    //말 선택하고나면 선택표시마크 전부 지우기
    fun delsel(){

        for(i in 0..36){

            val btnId = resources.getIdentifier("imageButton"+i, "id", packageName)
            (findViewById<View>(btnId) as CustomImageButton).apply {
                    setImageResource(android.R.color.transparent)
            }

        }


    }

    //승패가 났는지 확인
    fun check(){

        for(i in 0..36){

            val btnId = resources.getIdentifier("imageButton"+i, "id", packageName)
            val selnumber = (findViewById<View>(btnId) as CustomImageButton).context.resources.getResourceEntryName((findViewById<View>(btnId) as CustomImageButton).id).substring(11).toInt()
            (findViewById<View>(btnId) as CustomImageButton).apply {
                if (sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2)||sameCheck(R.drawable.cb12)
                        ||sameCheck(R.drawable.qb1)||sameCheck(R.drawable.qb2)
                        ||sameCheck(R.drawable.qb1y)||sameCheck(R.drawable.qb2y) ||sameCheck(R.drawable.qb12y)
                        ||sameCheck(R.drawable.qb1p)||sameCheck(R.drawable.qb2p) ||sameCheck(R.drawable.qb12p)
                        ||sameCheck(R.drawable.cb1qb2y)||sameCheck(R.drawable.cb2qb1y)) {

                    checknumber = checknumber + selnumber
                }
            }

        }

    }

    fun check2(){

        for(i in 0..36){

            val btnId = resources.getIdentifier("imageButton"+i, "id", packageName)
            val selnumber = (findViewById<View>(btnId) as CustomImageButton).context.resources.getResourceEntryName((findViewById<View>(btnId) as CustomImageButton).id).substring(11).toInt()
            (findViewById<View>(btnId) as CustomImageButton).apply {
                if (sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2)||sameCheck(R.drawable.cr12)
                        ||sameCheck(R.drawable.qr1)||sameCheck(R.drawable.qr2)
                        ||sameCheck(R.drawable.qr1y)||sameCheck(R.drawable.qr2y)||sameCheck(R.drawable.qr12y)
                        ||sameCheck(R.drawable.qr1p)||sameCheck(R.drawable.qr2p)||sameCheck(R.drawable.qr12p)
                        ||sameCheck(R.drawable.cr1qr2y)||sameCheck(R.drawable.cr2qr1y)) {

                    checknumber = checknumber + selnumber
                }
            }

        }

    }


    //player1이 윷 던진 경우 명령 첫번째 1번 윷 던진다
    fun roll1(view: View){

        if(n == 0) {

            imageButton0.setBackgroundResource(R.drawable.base)
            firstroll1()
            val handler = Handler()
            handler.postDelayed({
                notice.setVisibility(View.VISIBLE)
                notice.bringToFront()
                notice.invalidate()

                n = 1
            },1200)

        }
    }

    // 첫번째 윷 던지고 말 이동을 classic으로 선택한 경우
    fun classic(view:View){

        if(n==1){

            notice.setVisibility(View.GONE)
            if(text1 == "윷" || text1 == "모"){
                firstroll1()
                val handler = Handler()
                handler.postDelayed({
                    if(text1 == "윷" || text1 == "모"){
                        firstroll1()
                        val handler = Handler()
                        handler.postDelayed({
                            if(text1 == "윷" || text1 == "모"){
                                firstroll1()
                            }
                        },2500)
                    }
                },1300)
            }

        }
        else if(m==1){
            notice.setVisibility(View.GONE)
            if(text2 == "윷" || text2 == "모"){
                firstroll2()
                val handler = Handler()
                handler.postDelayed({
                    if(text2 == "윷" || text2 == "모"){
                        firstroll2()
                        val handler = Handler()
                        handler.postDelayed({
                            if(text2 == "윷" || text2 == "모"){
                                firstroll2()
                            }
                        },2500)
                    }
                },1300)
            }
        }

    }

    // 첫번째 윷 던지고 말 이동을 quantum으로 선택한 경우
    fun quantum(view:View){

        if(n == 1) {

            checknumber = 0

            for(i in 0..36){

                val btnId = resources.getIdentifier("imageButton"+i, "id", packageName)
                val selnumber = (findViewById<View>(btnId) as CustomImageButton).context.resources.getResourceEntryName((findViewById<View>(btnId) as CustomImageButton).id).substring(11).toInt()
                (findViewById<View>(btnId) as CustomImageButton).apply {
                    if (sameCheck(R.drawable.cb1)||sameCheck(R.drawable.cb2)||sameCheck(R.drawable.cb12)) {

                        checknumber = checknumber + selnumber
                    }
                }

            }

            if(checknumber == 0){
                Toast.makeText(this,"양자 말로 만들 수 있는 말이 없습니다",Toast.LENGTH_SHORT).show()
                checknumber = 0
            }
            else{

                notice.setVisibility(View.GONE)
                checknumber = 0
                firstroll1()
                val handler = Handler()
                handler.postDelayed({

                    n = 2
                    showselect3()

                }, 1300)

            }

        }
        else if(m==1){

            checknumber = 0

            for(i in 0..36){

                val btnId = resources.getIdentifier("imageButton"+i, "id", packageName)
                val selnumber = (findViewById<View>(btnId) as CustomImageButton).context.resources.getResourceEntryName((findViewById<View>(btnId) as CustomImageButton).id).substring(11).toInt()
                (findViewById<View>(btnId) as CustomImageButton).apply {
                    if (sameCheck(R.drawable.cr1)||sameCheck(R.drawable.cr2)||sameCheck(R.drawable.cr12)) {

                        checknumber = checknumber + selnumber
                    }
                }

            }

            if(checknumber == 0){
                Toast.makeText(this,"양자 말로 바꿀 수 있는 말이 없습니다",Toast.LENGTH_SHORT).show()
                checknumber = 0
            }
            else{

                notice.setVisibility(View.GONE)
                checknumber = 0
                firstroll2()
                val handler = Handler()
                handler.postDelayed({

                    m = 2
                    showselect4()

                }, 1300)

            }

        }

    }

    //player1 차례에서 윷 던지는 이미지 및 다음단계(글씨표시, 마크표시) 진행
    fun firstroll1(){

        yut_ani1.layoutParams.width = yut_board.width-50
        yut_ani1.layoutParams.height = yut_board.width-50

        yut_ani1.setVisibility(View.VISIBLE)
        yut_ani1.bringToFront()
        yut_ani1.invalidate()

        n1 = (Math.random() * 100000) + 1
        n2 = n1.toInt()
        t1 = (Math.random() * 200) + 1000
        t2 = t1.toLong()



        val animation = yut_ani1.background as AnimationDrawable
        animation.start()

        val handler = Handler()
        handler.postDelayed({

            yut_ani1.setVisibility(View.GONE)

            animation.stop()


            if (n2 >= 1 && n2 <= 15765) {
                text1 = "도"
                yut1.setBackgroundResource(R.drawable.doe)
            } else if (n2 >= 15766 && n2 <= 21020) {
                text1 = "빽도"
                yut1.setBackgroundResource(R.drawable.backdoe)
            } else if (n2 >= 21021 && n2 <= 58040) {
                text1 = "개"
                yut1.setBackgroundResource(R.drawable.gae)
            } else if (n2 >= 58041 && n2 <= 87010) {
                text1 = "걸"
                yut1.setBackgroundResource(R.drawable.geol)
            } else if (n2 >= 87011 && n2 <= 95510) {
                text1 = "윷"
                yut1.setBackgroundResource(R.drawable.yut)
            } else {
                text1 = "모"
                yut1.setBackgroundResource(R.drawable.mo)
            }

            player1_text()

        }, t2)

    }

    //main에 player1의 말 표시하기
    fun player1_text(){

        var txt1 : String = t1_1.text.toString()
        var txt2 : String = t1_2.text.toString()
        var txt3 : String = t1_3.text.toString()
        var txt4 : String = t1_4.text.toString()


        if(txt1 == ""){
            t1_1.text = text1
        }
        else if(txt2 == ""){
            t1_2.text = text1
        }
        else if(txt3 == ""){
            t1_3.text = text1
        }
        else if(txt4 == ""){
            t1_4.text = text1
        }
        else {

            Toast.makeText(this,"텍스트 더 추가해야됨 ㄷㄷㄷ",Toast.LENGTH_LONG).show()

        }
    }

    //player2이 윷 던진 경우 명령 첫번째 1번 윷 던진다
    fun roll2(view: View){

        if(m == 0) {

            imageButton0.setBackgroundResource(R.drawable.base)
            firstroll2()
            val handler = Handler()
            handler.postDelayed({
                notice.setVisibility(View.VISIBLE)
                notice.bringToFront()
                notice.invalidate()

                m = 1
            },1200)

        }
    }

    //player2 차례에서 윷 던지는 이미지 및 다음단계(글씨표시, 마크표시) 진행
    fun firstroll2(){

        yut_ani1.layoutParams.width = yut_board.width-50
        yut_ani1.layoutParams.height = yut_board.width-50

        yut_ani1.setVisibility(View.VISIBLE)
        yut_ani1.bringToFront()
        yut_ani1.invalidate()

        n1 = (Math.random() * 100000) + 1
        n2 = n1.toInt()
        t1 = (Math.random() * 200) + 1000
        t2 = t1.toLong()



        val animation = yut_ani1.background as AnimationDrawable
        animation.start()

        val handler = Handler()
        handler.postDelayed({

            yut_ani1.setVisibility(View.GONE)

            animation.stop()


            if (n2 >= 1 && n2 <= 15765) {
                text2 = "도"
                yut2.setBackgroundResource(R.drawable.doe)
            } else if (n2 >= 15766 && n2 <= 21020) {
                text2 = "빽도"
                yut2.setBackgroundResource(R.drawable.backdoe)
            } else if (n2 >= 21021 && n2 <= 58040) {
                text2 = "개"
                yut2.setBackgroundResource(R.drawable.gae)
            } else if (n2 >= 58041 && n2 <= 87010) {
                text2 = "걸"
                yut2.setBackgroundResource(R.drawable.geol)
            } else if (n2 >= 87011 && n2 <= 95510) {
                text2 = "윷"
                yut2.setBackgroundResource(R.drawable.yut)
            } else {
                text2 = "모"
                yut2.setBackgroundResource(R.drawable.mo)
            }

            player2_text()

        }, t2)

    }


    //main에 player2의 말 표시하기
    fun player2_text(){

        var txt5 : String = t2_1.text.toString()
        var txt6 : String = t2_2.text.toString()
        var txt7 : String = t2_3.text.toString()
        var txt8 : String = t2_4.text.toString()


        if(txt5 == ""){
            t2_1.text = text2
        }
        else if(txt6 == "") {
            t2_2.text = text2
        }
        else if(txt7 == ""){
            t2_3.text = text2
        }
        else if(txt8 == ""){
            t2_4.text = text2
        }
        else {

            Toast.makeText(this,"텍스트 더 추가해야됨 ㄷㄷㄷ",Toast.LENGTH_LONG).show()

        }
    }




}
