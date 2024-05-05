package org.tuiasi.engine.renderer.modelLoader;

import org.lwjgl.assimp.AIImporterDesc;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ModelLoader {


    public static Model test(String path){
        Model model = new Model();

        System.out.println("Loading model from path: " + path);
        File file = new File(path);
        System.out.println(file.toString());
        if(!file.exists()){
            System.out.println("File does not exist");
            return model;
        }

        try {
            AIScene scene = Assimp.aiImportFile(path,
                    Assimp.aiProcess_Triangulate
                            | Assimp.aiProcess_FlipUVs
                    );

            AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));

            // create a float array to store 11 values for each vertex: position (3), normal (3), texture (2)
            float[] vertices = new float[mesh.mNumVertices() * 8];

            // place the values in the array
            for (int i = 0; i < mesh.mNumVertices(); i++) {
                vertices[i * 8] = mesh.mVertices().get(i).x();
                vertices[i * 8 + 1] = mesh.mVertices().get(i).y();
                vertices[i * 8 + 2] = mesh.mVertices().get(i).z();
                vertices[i * 8 + 3] = mesh.mNormals().get(i).x();
                vertices[i * 8 + 4] = mesh.mNormals().get(i).y();
                vertices[i * 8 + 5] = mesh.mNormals().get(i).z();
                vertices[i * 8 + 6] = mesh.mTextureCoords(0).get(i).x();
                vertices[i * 8 + 7] = mesh.mTextureCoords(0).get(i).y();
            }

            // create a float array to store the faces
            int[] indices = new int[mesh.mNumFaces() * 3];
            for (int i = 0; i < mesh.mNumFaces(); i++) {
                indices[i * 3] = mesh.mFaces().get(i).mIndices().get(0);
                indices[i * 3 + 1] = mesh.mFaces().get(i).mIndices().get(1);
                indices[i * 3 + 2] = mesh.mFaces().get(i).mIndices().get(2);
            }

            model.setVertices(vertices);
            model.setIndices(indices);

            System.out.println("Done");
        }catch (Exception e){
            e.printStackTrace();
        }

        return model;
    }
}
