package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Touchable
import java.util.*
import kotlin.math.min

class FlappyBird : ApplicationAdapter() {
    var batch: SpriteBatch? = null
    var backGround: Texture?= null
    var img: Texture? = null
    var size: Float = 0f
    var birdsState = mutableListOf<Texture>()
    var topPipe: Texture? = null
    var bottomPipe: Texture?= null
    var flapState = 0

    var birdY: Float = 0f
    var velocity: Float = 0f

    var gameState = 0
    var gravity = 2f

    var gap = 0f
    var maxPipeOffset = 0f
    var randomGenerator: Random? = null

    val pipeVelocity = 4
    val numberOfPipes = 4
    var distHorBwPipes = 0
    var pipeX = FloatArray(numberOfPipes)

    var distVertBwPipes = FloatArray(numberOfPipes)
    var birdCircle: Circle? = null
    var topPipeRect = mutableListOf<Rectangle>()
    var bottomPipeRect = mutableListOf<Rectangle>()

    var score = 0
    var scoringPipe = 0
    var bitmapFont: BitmapFont? = null
    var scoreTextures = mutableListOf<Texture>()
    var maindisplay: Texture?= null
    var gameOver: Texture? = null

    override fun create() {
        batch = SpriteBatch()
        //img = Texture("badlogic.jpg")

        size = min(
            (Gdx.graphics.width*0.1).toFloat(),
            (Gdx.graphics.height*0.1).toFloat()
        )

        birdCircle = Circle()

        bitmapFont = BitmapFont()
        bitmapFont!!.setColor(Color.WHITE)
        bitmapFont!!.data.scale(size/10)

        gap = size*6

        backGround = Texture("background-day.png")
        birdsState.add(Texture("bluebird-downflap.png"))
        birdsState.add(Texture("bluebird-midflap.png"))
        birdsState.add(Texture("bluebird-upflap.png"))
        birdsState.add(Texture("bluebird-midflap.png"))
        topPipe = Texture("toppipe.png")
        bottomPipe = Texture("bottompipe.png")
        maindisplay = Texture("message.png")
        gameOver = Texture("gameover.png")

        for(i in 0..9)
        {
            scoreTextures.add(Texture("${i}.png"))
        }

        birdY = (Gdx.graphics.height/2 - size/2)

        maxPipeOffset = (Gdx.graphics.height - gap - gap/2)

        randomGenerator = Random()

        distHorBwPipes = Gdx.graphics.width * 3/4

        for (i in 0 until numberOfPipes)
        {
            pipeX[i] = (Gdx.graphics.width + i* distHorBwPipes).toFloat()
            distVertBwPipes[i] = (randomGenerator!!.nextFloat() - 0.5f) * maxPipeOffset

            topPipeRect.add(Rectangle())
            bottomPipeRect.add(Rectangle())
        }

        startGame()
    }

    private fun startGame()
    {
        velocity = 0f
        score = 0
        scoringPipe = 0

        birdY = (Gdx.graphics.height/2 - size/2)
        for (i in 0 until numberOfPipes)
        {
            pipeX[i] = (Gdx.graphics.width + i* distHorBwPipes).toFloat()
            distVertBwPipes[i] = (randomGenerator!!.nextFloat() - 0.5f) * maxPipeOffset
        }
    }

    override fun render() {
        batch!!.begin()
        // Background

        if (gameState == 1)
        {
            batch!!.draw(
                backGround,0f, 0f,
                Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat())
            // Bird
            batch!!.draw(
                birdsState[flapState],
                (Gdx.graphics.width/2 - size/2),
                birdY,
                size,
                size)
            Gdx.app.log("draw check", "!!")

            if(pipeX[scoringPipe] < Gdx.graphics.width / 2)
            {
                score++

                Gdx.app.log("Score", "${score}")

                if(scoringPipe < numberOfPipes - 1)
                {
                    scoringPipe++
                }
                else
                {
                    scoringPipe = 0
                }
            }
            if(Gdx.input.justTouched() || Gdx.input.isTouched)
            {
                velocity = (-gap*0.046).toFloat()
                Gdx.app.log("moving", "!!")
            }

            for (i in 0 until numberOfPipes)
            {
                if(pipeX[i] < -size*2)
                {
                    pipeX[i] = pipeX[i] + numberOfPipes * this.distHorBwPipes
                    distVertBwPipes[i] = (randomGenerator!!.nextFloat() - 0.5f) * maxPipeOffset
                }
                else
                {
                    pipeX[i] = pipeX[i] - pipeVelocity
                }

                // Top Pipe
                batch!!.draw(
                    topPipe,
                    pipeX[i],
                    (Gdx.graphics.height / 2 + gap / 2) + distVertBwPipes[i],
                    size*2,
                    (Gdx.graphics.height / 2 - gap / 2) +
                            maxPipeOffset*0.5f
                )
                // Bottom Pipe
                batch!!.draw(
                    bottomPipe,
                    pipeX[i],
                    0f - (Gdx.graphics.height - gap - gap/2)*0.5f + distVertBwPipes[i],
                    size*2,
                    (Gdx.graphics.height/2 - gap/2) +
                            maxPipeOffset*0.5f
                )

                topPipeRect[i].set(
                    pipeX[i],
                    (Gdx.graphics.height / 2 + gap / 2) + distVertBwPipes[i],
                    size*2,
                    (Gdx.graphics.height / 2 - gap / 2) + maxPipeOffset*0.5f)
//                shapeRenderer!!.rect(
//                        topPipeRect[i].x,
//                        topPipeRect[i].y,
//                        topPipeRect[i].width,
//                        topPipeRect[i].height
//                )

                bottomPipeRect[i].set(
                    pipeX[i],
                    0f - (Gdx.graphics.height - gap - gap/2)*0.5f + distVertBwPipes[i],
                    size*2,
                    (Gdx.graphics.height/2 - gap/2) + maxPipeOffset*0.5f)
//                shapeRenderer!!.rect(
//                        bottomPipeRect[i].x,
//                        bottomPipeRect[i].y,
//                        bottomPipeRect[i].width,
//                        bottomPipeRect[i].height
//                )
            }
            if(birdY > 0 || velocity < 0)
            {
                velocity += gravity
                birdY -= velocity
                Gdx.app.log("birdY", "changing")
            }

            when(flapState)
            {
                0 -> flapState = 1
                1 -> flapState = 2
                2 -> flapState = 3
                3 -> flapState = 0
            }
            val scoreDigits = mutableListOf<Int>()
            var _score = score
            if (_score == 0)
            {
                batch!!.draw(
                    scoreTextures[0],
                    size,
                    Gdx.graphics.height - size*2,
                    size,
                    size)
            }
            else
            {
                while (_score > 0)
                {
                    scoreDigits.add(_score % 10)
                    _score /= 10
                }
                scoreDigits.reverse()
                Gdx.app.log("scoreDigits", "$scoreDigits")
                for (i in 0 until scoreDigits.count())
                {
                    batch!!.draw(
                        scoreTextures[scoreDigits[i]],
                        size*(i+1),
                        Gdx.graphics.height - size*2,
                        size,
                        size)
                }
            }
        }
        else if(gameState == 0)
        {
            batch!!.draw(
                maindisplay,
                size,
                size,
                Gdx.graphics.width*0.8.toFloat(),
                Gdx.graphics.height*0.8.toFloat())

            if(Gdx.input.justTouched())
            {
                gameState = 1
                Gdx.app.log("start", "!!")
            }
        }
        else if(gameState == 2)
        {
            batch!!.draw(
                gameOver,
                Gdx.graphics.width / 2 - size*3,
                Gdx.graphics.height /2 - size/2,
                size*6,
                size)

            if(Gdx.input.justTouched())
            {
                startGame()
                Gdx.app.log("restart", "!!")
                gameState = 1
            }
        }

        birdCircle!!.set(
            (Gdx.graphics.width/2f),
            birdY + size/2,
            size/2)

        for (i in 0 until numberOfPipes)
        {
            if(Intersector.overlaps(birdCircle, topPipeRect[i]) ||
                Intersector.overlaps(birdCircle, bottomPipeRect[i]))
            {
                Gdx.app.log("Collision", "Detected!")
                gameState = 2
            }
        }
        batch!!.end()
    }

    //override fun dispose() {
        //batch!!.dispose()
        //img!!.dispose()
    //}
}