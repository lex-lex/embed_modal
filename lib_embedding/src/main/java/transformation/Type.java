package transformation;

import java.util.HashMap;

public class Type {
    private static HashMap<String,Type> normalizedTypeStringToType = new HashMap<>();
    private static HashMap<Type,Integer> typeToCounter = new HashMap<>();

    private String normalizedType;
    private Type(String normalizedType){
        this.normalizedType = normalizedType;
    }

    public static Type getTypeFromString(String type){
        String normalizedType = getNormalizedType(type);
        if (!normalizedTypeStringToType.containsKey(normalizedType)){
            Type typeSingleton = new Type(normalizedType);
            typeToCounter.put(typeSingleton, typeToCounter.size());
            normalizedTypeStringToType.put(normalizedType, typeSingleton);
        }
        return normalizedTypeStringToType.get(normalizedType);
    }

    public int getTypeIdentifier(){
        return typeToCounter.get(this);
    }

    @Override
    public String toString(){
        return normalizedType;
    }

    /*
    @Override
    public boolean equals(Object a){
        if (a.toString().equals(this.toString())) return true;
        return false;
    }*/

    public static void reset(){
        normalizedTypeStringToType.clear();
        typeToCounter.clear();
    }

    private static String getNormalizedType(String type){
        return type; // TODO braces, etc.
    }


}
