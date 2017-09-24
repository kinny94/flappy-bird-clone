package com.arjundass.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
    Texture gameover;
    //ShapeRenderer shapeRenderer;

    Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    Circle birdCircle;

    int score = 0;
    int scoringTubes = 0;
    BitmapFont font;

    int gameState = 0;
    float gravity = 2;

    Texture topTube;
    Texture bottomTube;
    float gap = 800;
    float maxTubeOffset;
    Random randomNumberGenerator;

    float tubeVelocity = 6;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset  = new float[numberOfTubes];
    float distanceBetweenTubes;

    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;

	@Override
	public void create () {
		batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameover = new Texture("gameover.png");

       // shapeRenderer = new ShapeRenderer(); // allow us to render shape
        birdCircle = new Circle();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);


        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");


        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 200;
        randomNumberGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2;

        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        startGame();

	}

	public void startGame(){
        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
        for(int i=0; i<numberOfTubes; i++){
            tubeOffset[i] = (randomNumberGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 800);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth()  + (i * distanceBetweenTubes);

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();

        }
    }

	@Override
	public void render () {

        //create the background before anything else
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Render method is gonna run again and again
        if(gameState == 1){

            if(tubeX[scoringTubes] < Gdx.graphics.getWidth()/2){
                score++;

                Gdx.app.log("Score", String.valueOf(score));
                if(scoringTubes < numberOfTubes - 1){
                    scoringTubes++;
                }else{
                    scoringTubes = 0;
                }
            }

            // Decreasing the velocity in negative to bring it up
            if(Gdx.input.justTouched()){
                //Gdx.app.log("Touched!", "Touched");
                velocity = -30;
            }
            // Increasing the velocity each time the render method is invoked

            for(int i=0; i<numberOfTubes; i++) {

                if(tubeX[i] < -topTube.getWidth()){
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomNumberGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 800);
                }else{
                    tubeX[i] = tubeX[i] - tubeVelocity;

                }

                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight()/2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap/2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap/2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }





            if(birdY > 0 ){
                velocity = velocity + gravity;
                birdY -=velocity;
            }else{
                gameState = 2;
            }

        }else if(gameState == 0){

            if(Gdx.input.justTouched()){
                gameState = 1;
            }
        } else if(gameState == 2){
            batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight() /2);
            if(Gdx.input.justTouched()){
                gameState = 1;
                startGame();
                score = 0;
                scoringTubes = 0;
                velocity = 0;
            }
        }

        if(flapState == 0){
            flapState = 1;
        }else{
            flapState = 0;
        }


        batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY);
        font.draw(batch, String.valueOf(score), 100, 200);
        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.BLUE);
	    //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for(int i=0;i<numberOfTubes; i++){
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap/2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

            if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){
                gameState = 2;
            }

        }
        batch.end();
        //shapeRenderer.end();
	}
}
