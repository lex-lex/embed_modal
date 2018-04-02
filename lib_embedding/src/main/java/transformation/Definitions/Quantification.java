package transformation.Definitions;

import transformation.Type;

public class Quantification {

    private static final String w = Common.w;

    /***************************************************************************
     * Constant Quantification TH0
     ***************************************************************************/

    public static String embedded_forall(Type type){
        return "mforall_const_" + type.getTypeIdentifier();
    }

    public static String embedded_exists(Type type){
        return "mexists_const_" + type.getTypeIdentifier();
    }

    public static String mforall_const_th0(Type type){
        StringBuilder sb = new StringBuilder();
        // concrete type sentence from
        // thf( mforall_const_type , type , ( mforall_const : !> [T:$tType] : (T > " + w + " > $o) > " + w + " > $o ) ).
        sb.append("thf( mforall_const_type_");
        sb.append(type.getTypeIdentifier());
        sb.append(" , type , ( mforall_const_");
        sb.append(type.getTypeIdentifier());
        sb.append(" : ( ( ( ");
        sb.append(type);
        sb.append(" ) > ( " + w + " > $o ) ) > " + w + " > $o ) ) ).");
        sb.append("\n");
        // concrete definition sentence from
        // thf( mforall_const , definition , ( mforall_const = (
        // ^ [A:T>" + w + ">$o,W:" + w + "] : ! [X:T] : (A @ X @ W)
        //        ))).
        sb.append("thf( mforall_const_");
        sb.append(type.getTypeIdentifier());
        sb.append(" , definition , ( mforall_const_");
        sb.append(type.getTypeIdentifier());
        sb.append(" = ( ^ [A:(");
        sb.append(type);
        sb.append(")>" + w + ">$o,W:" + w + "] : ! [X:(");
        sb.append(type);
        sb.append(")] : (A @ X @ W)");
        sb.append("))).");
        return sb.toString();
    }

    public static String mexists_const_th0(Type type){
        return mforall_const_th0(type).replaceAll("forall","exists").replaceAll("!","?");
    }

    /***************************************************************************
     * Constant Quantification TH1
     ***************************************************************************/
    /*
    public static final String mforall_const_th1 = "" +
            "thf( mforall_const_type , type , ( mforall_const : !> [T:$tType] : (T > " + w + " > $o) > " + w + " > $o ) ).\n" +
            "thf( mforall_const , definition , ( mforall_const = (" +
            "^ [A:T>" + w + ">$o,W:" + w + "] : ! [X:T] : (A @ X @ W)" +
            "))).";

    public static final String mexists_const_th1 = "" +
            "thf( mforall_const_type , type , ( mexists_const : !> [T:$tType] : (T > " + w + " > $o) > " + w + " > $o ) ).\n" +
            "thf( mforall_const , definition , ( mexists_const = (" +
            "^ [A:T>" + w + ">$o,W:" + w + "] : ? [X:T] : (A @ X @ W)" +
            "))).";
    */

    /***************************************************************************
     * Varying Quantification TH0
     ***************************************************************************/

    public static String embedded_forall_varying(Type type){
        return "mforall_vary_" + type.getTypeIdentifier();
    }

    public static String embedded_exists_varying(Type type){
        return "mexists_vary_" + type.getTypeIdentifier();
    }

    public static final String mcurrentworld = "" +
            "thf( mcurrentworld_type , type , ( mcurrentworld: " + w + " ) ).";

    /** Exists in world predicate for a certain type */
    public static String eiw_th0(Type type){
        StringBuilder sb = new StringBuilder();
        // concrete type sentence from
        // thf( exists_in_world_type , type , ( eiw : !> [T:$tType] : (T > " + w + " > $o) ) ).
        sb.append("thf( exists_in_world_type_");
        sb.append(type.getTypeIdentifier());
        sb.append(" , type , ( eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" : ( ( ( ");
        sb.append(type);
        sb.append(" ) > ( " + w + " > $o ) ) ) ) ).");
        sb.append("\n");
        // non-emptyness axiom
        // thf( eiw_nonempty , axiom , ( eiw_nonempty = (
        // ! [W:" + w + "]: ? [X:T] : eiw @ X @ W
        //        ))).
        sb.append("thf( eiw_nonempty_");
        sb.append(type.getTypeIdentifier());
        sb.append(" , axiom , (");
        sb.append("! [W:" + w + "]: ( ? [X:("+type+")] : (");
        sb.append("eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" @ X @ W");
        sb.append(")))).");
        return sb.toString();
    }

    /** Axiom for the existence of a constant `constant` of type `type`. */
    public static String constant_eiw_th0(String constant, Type type){
        StringBuilder sb = new StringBuilder();
        // non-emptyness axiom
        // thf( eiw_nonempty , axiom , ( eiw_nonempty = (
        // ! [W:" + w + "]: ? [X:T] : eiw @ X @ W
        //        ))).
        sb.append("thf( eiw_");
        sb.append(constant);
        sb.append(" , axiom , (");
        sb.append("! [W:" + w + "]: ( (");
        sb.append("eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" @ ");
        sb.append(constant);
        sb.append(" @ W");
        sb.append(")))).");
        return sb.toString();
    }

    /** Axiom for cumulative domains for type `type`. */
    public static String cumulative_eiw_th0(Type type) {
        // TODO alter for multimodal systems
        StringBuilder sb = new StringBuilder();
        // cumulative axiom for each relation r
        // thf( eiw_cumul , axiom , (
        // ! [W:" + w + ", V:" + w + ", C: T]: ((rel_r @ W @ V) => ((eiw @ C @ W) => (eiw @ C @ V)))
        // )).
//        for (String relation : relation_symbols) {
        sb.append("thf( eiw_cumul_");
        sb.append(type.getTypeIdentifier());
        sb.append("_");
        sb.append("r"); // sb.append(relation);
        sb.append(" , axiom , (");
        sb.append("! [W:" + w + ",V:" + w + ",C:" + type + "]: ( (");
        sb.append(AccessibilityRelation.accessibility_relation_prefix);
        // sb.append(relation_postfix);
        sb.append(" @ W @ V) => ((eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" @ C @ W) => (eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" @ C @ V)) ");
        sb.append("))).");
//        }
        return sb.toString();
    }

    /** Axiom for decreasing domains for type `type`. */
    public static String decreasing_eiw_th0(Type type) {
        // TODO alter for multimodal systems
        StringBuilder sb = new StringBuilder();
        // decreasing axiom for each relation r
        // thf( eiw_decre , axiom , (
        // ! [W:" + w + ", V:" + w + ", C: T]: ((rel_r @ W @ V) => ((eiw @ C @ V) => (eiw @ C @ W)))
        // )).
//        for (String relation : relation_symbols) {
        sb.append("thf( eiw_decre_");
        sb.append(type.getTypeIdentifier());
        sb.append("_");
        sb.append("r"); // sb.append(relation);
        sb.append(" , axiom , (");
        sb.append("! [W:" + w + ",V:" + w + ",C:" + type + "]: ( (");
        sb.append(AccessibilityRelation.accessibility_relation_prefix);
        // sb.append(relation_postfix);
        sb.append(" @ W @ V) => ((eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" @ C @ V) => (eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" @ C @ W)) ");
        sb.append("))).");
//        }
        return sb.toString();
    }

    /** Declaration of varying domain quantifier */
    public static String mforall_varying_th0(Type type){
        StringBuilder sb = new StringBuilder();
        // concrete type sentence from
        // thf( mforall_const_type , type , ( mforall_const : !> [T:$tType] : (T > " + w + " > $o) > " + w + " > $o ) ).
        sb.append("thf(mforall_vary_type_");
        sb.append(type.getTypeIdentifier());
        sb.append(" , type , ( mforall_vary_");
        sb.append(type.getTypeIdentifier());
        sb.append(" : ( ( ( ");
        sb.append(type);
        sb.append(" ) > ( " + w + " > $o ) ) > " + w + " > $o ) ) ).");
        sb.append("\n");
        // concrete definition sentence from
        // thf( mforall_const , definition , ( mforall_const = (
        // ^ [A:T>" + w + ">$o,W:" + w + "] : ! [X:T] : ((eiw  @ X @ W) => (A @ X @ W))
        //        ))).
        sb.append("thf(mforall_vary_");
        sb.append(type.getTypeIdentifier());
        sb.append(" , definition , ( mforall_vary_");
        sb.append(type.getTypeIdentifier());
        sb.append(" = ( ^ [A:(");
        sb.append(type);
        sb.append(")>" + w + ">$o,W:" + w + "] : ! [X:(");
        sb.append(type);
        sb.append(")] : ((eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" @ X @ W) => (A @ X @ W))");
        sb.append("))).");
        return sb.toString();
    }

    /** Declaration of varying domain quantifier */
    public static String mexists_varying_th0(Type type){
        StringBuilder sb = new StringBuilder();
        sb.append("thf(mexists_vary_type_");
        sb.append(type.getTypeIdentifier());
        sb.append(" , type , ( mexists_vary_");
        sb.append(type.getTypeIdentifier());
        sb.append(" : ( ( ( ");
        sb.append(type);
        sb.append(" ) > ( " + w + " > $o ) ) > " + w + " > $o ) ) ).");
        sb.append("\n");
        // concrete definition sentence from
        // thf( mexists_const , definition , ( mexists_const = (
        // ^ [A:T>" + w + ">$o,W:" + w + "] : ? [X:T] : ((eiw  @ X @ W) & (A @ X @ W))
        //        ))).
        sb.append("thf(mexists_vary_");
        sb.append(type.getTypeIdentifier());
        sb.append(" , definition , ( mexists_vary_");
        sb.append(type.getTypeIdentifier());
        sb.append(" = ( ^ [A:(");
        sb.append(type);
        sb.append(")>" + w + ">$o,W:" + w + "] : ? [X:(");
        sb.append(type);
        sb.append(")] : ((eiw_");
        sb.append(type.getTypeIdentifier());
        sb.append(" @ X @ W) & (A @ X @ W))");
        sb.append("))).");
        return sb.toString();
    }


    /***************************************************************************
     * Varying Quantification TH1
     ***************************************************************************/
    // TODO

}
