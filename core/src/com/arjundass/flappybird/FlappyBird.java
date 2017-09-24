package com.arjundass.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;

    Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;

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

	@Override
	public void create () {
		batch = new SpriteBatch();
        background = new Texture("bg.png");

        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 200;
        randomNumberGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2;

        for(int i=0; i<numberOfTubes; i++){
            tubeOffset[i] = (randomNumberGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 800);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + (i * distanceBetweenTubes);

        }

	}

	@Override
	public void render () {

        //create the background before anything else
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Render method is gonna run again and again
        if(gameState != 0){

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
            }





            if(birdY > 0 || velocity < 0){
                velocity = velocity + gravity;
                birdY -=velocity;
            }

        }else{

            if(Gdx.input.justTouched()){
                gameState = 1;
            }
        }

        if(flapState == 0){
            flapState = 1;
        }else{
            flapState = 0;
        }


        batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY);
        batch.end();

	}
}
