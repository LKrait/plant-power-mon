package com.example.plantpowermonitoring

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.min

class DashboardView(context: Context) : View(context) {
    private val bg = Color.rgb(4, 18, 34)
    private val card = Color.rgb(7, 31, 55)
    private val card2 = Color.rgb(8, 39, 67)
    private val line = Color.rgb(18, 70, 108)
    private val white = Color.WHITE
    private val muted = Color.rgb(170, 195, 218)
    private val blue = Color.rgb(38, 145, 255)
    private val green = Color.rgb(33, 235, 145)
    private val yellow = Color.rgb(255, 193, 7)
    private val red = Color.rgb(255, 75, 75)
    private val purple = Color.rgb(165, 100, 255)

    private val p = Paint(Paint.ANTI_ALIAS_FLAG)
    private var scrollY = 0f
    private var downY = 0f
    private var lastY = 0f
    private var dragging = false
    private var contentHeight = 0f

    private val feeders = listOf(
        Triple("Feeder 1", 75, 104), Triple("Feeder 2", 62, 86), Triple("Feeder 3", 48, 66), Triple("Feeder 4", 90, 125),
        Triple("Feeder 5", 120, 165), Triple("Feeder 6", 68, 92), Triple("Feeder 7", 54, 75), Triple("Feeder 8", 88, 118),
        Triple("Feeder 9", 76, 102), Triple("Feeder 10", 42, 58), Triple("Feeder 11", 110, 148), Triple("Feeder 12", 95, 130),
        Triple("Feeder 13", 60, 83), Triple("Feeder 14", 130, 176), Triple("Feeder 15", 70, 95), Triple("Feeder 16", 85, 115)
    )

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        c.drawColor(bg)
        c.save()
        c.translate(0f, -scrollY)
        val w = width.toFloat()
        var y = 0f
        y = drawHeader(c, y, w)
        y = drawTotalLoad(c, y, w)
        y = drawPowerSources(c, y, w)
        y = drawDistribution(c, y, w)
        y = drawFeeders(c, y, w)
        y = drawCharts(c, y, w)
        y += 80f
        contentHeight = y
        c.restore()
        drawBottomBar(c)
    }

    private fun drawHeader(c: Canvas, y0: Float, w: Float): Float {
        val h = 76f
        p.color = Color.rgb(5, 25, 45); c.drawRect(0f, 0f, w, h, p)
        text(c, "☰", 24f, 50f, 31f, white, true)
        text(c, "⚡", 68f, 52f, 32f, blue, true)
        text(c, "Plant Power Monitoring", 108f, 45f, 21f, white, true)
        text(c, "ONLINE", w - 88f, 27f, 11f, green, true)
        text(c, "●", w - 108f, 27f, 12f, green, true)
        text(c, "17 Sep 2026  10:24", w - 190f, 57f, 10f, muted, false)
        return h + 10f
    }

    private fun drawTotalLoad(c: Canvas, y: Float, w: Float): Float {
        val h = 205f; card(c, 14f, y, w-14f, y+h, green)
        text(c, "⚡  TOTAL PLANT LOAD", 30f, y+30f, 17f, white, true)
        text(c, "1,248 kW", 42f, y+98f, 34f, white, true)
        text(c, "Current Load", 52f, y+121f, 12f, muted, false)
        // gauge
        p.style = Paint.Style.STROKE; p.strokeWidth = 15f; p.strokeCap = Paint.Cap.ROUND; p.color = Color.rgb(55,80,108)
        c.drawArc(45f, y+45f, 245f, y+190f, 145f, 250f, false, p)
        p.color = green; c.drawArc(45f, y+45f, 245f, y+190f, 145f, 155f, false, p)
        p.style = Paint.Style.FILL
        statBox(c, 285f, y+50f, w-30f, y+93f, "Peak", "1,420 kW")
        statBox(c, 285f, y+101f, w-30f, y+144f, "Power Factor", "0.96")
        statBox(c, 285f, y+152f, w-30f, y+195f, "Frequency", "50.0 Hz")
        return y+h+10f
    }

    private fun drawPowerSources(c: Canvas, y: Float, w: Float): Float {
        val h = 205f; card(c, 14f, y, w-14f, y+h, blue)
        text(c, "⚡  POWER SOURCES", 30f, y+30f, 17f, white, true)
        val gap = 8f; val left=25f; val right=w-25f; val mid=w/2f
        source(c,left,y+48f,mid-gap,y+175f,"Generator","680 kW","400 V","98 A",green)
        source(c,mid+gap,y+48f,right,y+175f,"Utility","568 kW","398 V","82 A",blue)
        p.color = green; c.drawRect(left,y+184f,mid-3f,y+194f,p)
        p.color = blue; c.drawRect(mid+3f,y+184f,right,y+194f,p)
        text(c,"54%",mid-38f,y+194f,9f,white,true); text(c,"46%",mid+8f,y+194f,9f,white,true)
        return y+h+10f
    }

    private fun drawDistribution(c: Canvas, y: Float, w: Float): Float {
        val h=175f; card(c,14f,y,w-14f,y+h,yellow)
        text(c,"◔  LOAD DISTRIBUTION",30f,y+30f,17f,white,true)
        p.style=Paint.Style.STROKE; p.strokeWidth=22f; p.color=blue; c.drawArc(35f,y+50f,170f,y+185f,-90f,180f,false,p)
        p.color=Color.rgb(255,130,30); c.drawArc(35f,y+50f,170f,y+185f,90f,135f,false,p)
        p.color=purple; c.drawArc(35f,y+50f,170f,y+185f,225f,55f,false,p); p.style=Paint.Style.FILL
        text(c,"1,248",70f,y+116f,18f,white,true); text(c,"kW",82f,y+136f,10f,muted,false)
        val x=205f
        distRow(c,x,y+65f,"Feeders 1–8","620 kW",blue)
        distRow(c,x,y+95f,"Feeders 9–16","628 kW",Color.rgb(255,130,30))
        distRow(c,x,y+125f,"Auxiliary","120 kW",purple)
        distRow(c,x,y+155f,"Lighting","80 kW",yellow)
        return y+h+10f
    }

    private fun drawFeeders(c: Canvas, y: Float, w: Float): Float {
        val rows=4; val h=48f+rows*68f+14f; card(c,14f,y,w-14f,y+h,blue)
        text(c,"▦  FEEDERS (16)",30f,y+30f,17f,white,true)
        text(c,"● ON",w-82f,y+30f,10f,green,true)
        val cols=2; val gap=8f; val cw=(w-28f-gap)/2f
        for(i in feeders.indices){
            val row=i/cols; val col=i%cols
            val x=20f+col*(cw+gap); val yy=y+43f+row*68f
            val warn=(i==4 || i==14); p.color=if(warn) yellow else green
            roundRect(c,x,yy,x+cw,yy+59f,10f,card2); c.drawRect(x,yy,x+5f,yy+59f,p)
            text(c,"${i+1}",x+15f,yy+23f,15f,white,true)
            text(c,feeders[i].first,x+43f,yy+20f,12f,white,true)
            text(c,"${feeders[i].second} kW",x+43f,yy+43f,11f,muted,false)
            text(c,"${feeders[i].third} A",x+125f,yy+43f,11f,muted,false)
            text(c,if(warn) "WARN" else "ON",x+cw-44f,yy+20f,9f,if(warn) yellow else green,true)
        }
        return y+h+10f
    }

    private fun drawCharts(c: Canvas, y: Float, w: Float): Float {
        val h=185f; val half=(w-28f-8f)/2f
        chartCard(c,14f,y,14f+half,y+h,"LOAD TREND",false)
        chartCard(c,22f+half,y,w-14f,y+h,"ENERGY CONSUMPTION",true)
        return y+h
    }

    private fun chartCard(c: Canvas,l:Float,t:Float,r:Float,b:Float,title:String,bars:Boolean){
        roundRect(c,l,t,r,b,12f,card); text(c,title,l+14f,t+28f,13f,white,true)
        val x=l+25f; val y=t+55f; val rw=r-l-38f; val rh=95f
        p.color=line; p.style=Paint.Style.STROKE; p.strokeWidth=1f
        for(i in 0..4) c.drawLine(x,y+i*rh/4,x+rw,y+i*rh/4,p)
        if(bars){
            p.style=Paint.Style.FILL
            for(i in 0 until 10){ val bh=20f+(i%5)*10f; p.color=if(i%2==0) blue else green; c.drawRect(x+i*(rw/10f)+4,y+rh-bh,x+i*(rw/10f)+18,y+rh,p) }
        } else {
            lineGraph(c,x,y,rw,rh,green,0); lineGraph(c,x,y+15,rw,rh-15,blue,1); lineGraph(c,x,y+28,rw,rh-28,yellow,2)
        }
        text(c,if(bars) "kWh" else "kW",x,t+49f,9f,muted,false)
        text(c,"00",x,b-12f,8f,muted,false); text(c,"12",x+rw/2-5,b-12f,8f,muted,false); text(c,"24",x+rw-12,b-12f,8f,muted,false)
    }

    private fun lineGraph(c:Canvas,x:Float,y:Float,rw:Float,rh:Float,color:Int,offset:Int){
        p.style=Paint.Style.STROKE; p.strokeWidth=2.5f; p.color=color; val path=Path()
        for(i in 0..24){ val xx=x+i*rw/24f; val yy=y+rh*(0.35f+0.10f*((i+offset*3)%5))+if(offset==0) -i*0.7f else 0f; if(i==0) path.moveTo(xx,yy) else path.lineTo(xx,yy) }
        c.drawPath(path,p); p.style=Paint.Style.FILL
    }

    private fun source(c:Canvas,l:Float,t:Float,r:Float,b:Float,name:String,load:String,voltage:String,current:String,accent:Int){
        roundRect(c,l,t,r,b,10f,card2); text(c,name,l+12f,t+25f,14f,white,true); text(c,"● Online",l+12f,t+45f,10f,green,true)
        text(c,"Load",l+12f,t+70f,10f,muted,false); text(c,load,r-70f,t+70f,11f,white,true)
        text(c,"Voltage",l+12f,t+91f,10f,muted,false); text(c,voltage,r-55f,t+91f,11f,white,true)
        text(c,"Current",l+12f,t+112f,10f,muted,false); text(c,current,r-55f,t+112f,11f,white,true)
    }
    private fun statBox(c:Canvas,l:Float,t:Float,r:Float,b:Float,label:String,value:String){ roundRect(c,l,t,r,b,8f,card2); text(c,label,l+12f,t+17f,9f,muted,false); text(c,value,l+12f,t+34f,13f,white,true) }
    private fun distRow(c:Canvas,x:Float,y:Float,label:String,value:String,color:Int){ p.color=color;c.drawCircle(x+7,y-4,5f,p);text(c,label,x+18,y,11f,white,false);text(c,value,x+170,y,11f,white,true) }
    private fun card(c:Canvas,l:Float,t:Float,r:Float,b:Float,accent:Int){roundRect(c,l,t,r,b,12f,card);p.color=accent;c.drawRect(l,t,r,t+4f,p)}
    private fun roundRect(c:Canvas,l:Float,t:Float,r:Float,b:Float,rad:Float,color:Int){p.style=Paint.Style.FILL;p.color=color;c.drawRoundRect(l,t,r,b,rad,rad,p)}
    private fun text(c:Canvas,s:String,x:Float,y:Float,size:Float,color:Int,bold:Boolean){p.typeface=if(bold) Typeface.create("sans",Typeface.BOLD) else Typeface.create("sans",Typeface.NORMAL);p.textSize=size;p.color=color;p.style=Paint.Style.FILL;c.drawText(s,x,y,p)}

    private fun drawBottomBar(c:Canvas){
        val h=64f; val top=height-h; p.color=Color.rgb(5,27,48);c.drawRect(0f,top,width.toFloat(),height.toFloat(),p)
        val labels=arrayOf("Dashboard","Feeders","Trends","Alarms","Settings")
        for(i in labels.indices){ val x=width*(i+0.5f)/5f; text(c,labels[i],x-p.measureText(labels[i])/2,top+45,9f,if(i==0) blue else muted,i==0) }
        text(c,"⌂",width/10f-10,top+25,22f,blue,true); text(c,"⚡",width*3/10f-9,top+25,19f,muted,true); text(c,"⌁",width*5/10f-9,top+25,22f,muted,true); text(c,"♧",width*7/10f-9,top+25,20f,muted,true); text(c,"⚙",width*9/10f-10,top+25,20f,muted,true)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when(e.action){
            MotionEvent.ACTION_DOWN -> {downY=e.y;lastY=e.y;dragging=false;return true}
            MotionEvent.ACTION_MOVE -> { val dy=e.y-lastY; if(kotlin.math.abs(e.y-downY)>8) dragging=true; scrollY=(scrollY-dy).coerceIn(0f,(contentHeight-(height-64f)).coerceAtLeast(0f));lastY=e.y;invalidate();return true }
            MotionEvent.ACTION_UP -> { if(!dragging && e.y>height-75) { Toast.makeText(context,"Navigation is ready for the next module.",Toast.LENGTH_SHORT).show() }; return true }
        }
        return true
    }
}
