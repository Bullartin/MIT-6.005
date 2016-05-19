package sat;

import static org.junit.Assert.*;

import org.junit.Test;

import sat.env.Bool;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

public class SATSolverTest {

    Variable va = new Variable("a");
    Variable vb = new Variable("b");
    Variable vc = new Variable("c");
    Literal a = PosLiteral.make(va);
    Literal b = PosLiteral.make(vb);
    Literal c = PosLiteral.make(vc);
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();
    Clause canb = make(a, nb);
    Clause cab = make(a, b);
    Clause cnbc = make(nb, c);

    // make sure assertions are turned on!
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testSolve1() {
        Formula f1 = new Formula(canb);
        f1 = f1.addClause(cab);
        Environment e = SATSolver.solve(f1);
        assertEquals(Bool.TRUE, va.eval(e));
        // assertEquals(Bool.UNDEFINED, vb.eval(e));
    }

    @Test
    public void testSolve2() {
        Formula f1 = new Formula(make(a));
        f1 = f1.addClause(make(b));
        f1 = f1.addClause(make(a));
        f1 = f1.addClause(make(nb));
        Environment e = SATSolver.solve(f1);
        assertEquals(null, e);
    }

    @Test
    public void testSolve3() {
        Formula f1 = new Formula(make(a));
        f1 = f1.addClause(make(b));
        f1 = f1.addClause(cnbc);
        Environment e = SATSolver.solve(f1);
        assertEquals(Bool.TRUE, va.eval(e));
        assertEquals(Bool.TRUE, vb.eval(e));
        assertEquals(Bool.TRUE, vc.eval(e));
    }

    private Clause make(Literal... e) {
        Clause c = new Clause();
        for (int i = 0; i < e.length; ++i) {
            c = c.add(e[i]);
        }
        return c;
    }

}