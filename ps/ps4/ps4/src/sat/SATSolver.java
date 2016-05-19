package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // if (formula.getSize() == 0) {
        // return new Environment();
        // }
        // for (Clause c: formula) {
        // if (c.isEmpty()) {
        // return null;
        // }
        // }
        return solve(formula.getClauses(), new Environment());
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if (clauses.size() == 0) {
            return env;
        }
        Clause c = clauses.first(); // need pick smallest clauses
        if (c.isEmpty()) {
            return null;
        }
        Literal l = c.chooseLiteral();
        Environment result;
        result = solve(substitute(clauses, l), env.putTrue(l.getVariable()));
        if (result != null) {
            return result;
        }
        result = solve(substitute(clauses, l.getNegation()), env.putFalse(l.getVariable()));
        return result;
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal l) {
        ImList<Clause> result = new EmptyImList<Clause>();
        for (Clause c : clauses) {
            Clause temp = c.reduce(l);
            if (temp != null) {
                result = result.add(c.reduce(l));
            }
        }
        return result;
    }

}
