package Archivos;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FindDirectory {
    //Copiar TestsResources al disco C
    public static void main(String[] args) throws Exception {
        //printMostLikelyPath(pathListFromStrings("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Skyrim Special Edition"));
        //printMostLikelyPath(pathListFromStrings("C:\\Users\\Admin\\Documents\\My Games\\Octopath_Traveler2"));
        //printMostLikelyPath(pathListFromStrings("C:\\Users\\Admin\\Documents\\My Games\\Terraria\\Worlds"));
        //printMostLikelyPath(pathListFromStrings("C:\\Program Files (x86)\\Steam\\steamapps\\comun\\Skyrim Special Edition"));
        //printMostLikelyPath(pathListFromStrings("C:\\Users\\JoseLuis\\Documents\\My Games\\Octopath_Traveler2"));
        printMostLikelyPath(pathListFromStrings("C:\\Users\\Admin\\Documents\\My_Games\\Terraria\\Worlds", "C:\\Users\\Admin\\Documentos\\My Games\\Terraria\\Worlds"));

    }
    private static List<Path> pathListFromStrings(String... paths){
        List<Path> laLista = new ArrayList<>();
        for(String p : paths) {
            laLista.add(Paths.get(p));
        }
        return laLista;
    }
    private static void printMostLikelyPath(List<Path> paths){
        try {
            System.out.println(AlgoritmoPredictivoPaths.predecir(paths));
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
