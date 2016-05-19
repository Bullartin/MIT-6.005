package sat.formula;

import static org.junit.Assert.*;
import org.junit.Test;

import immutable.ImList;
import immutable.NonEmptyImList;
import sat.env.Variable;

public class FormulaTest {
    Clause empty = make();
    Literal p = PosLiteral.make("P");
    Literal q = PosLiteral.make("Q");
    Literal r = PosLiteral.make("R");
    Literal s = PosLiteral.make("S");
    Literal np = p.getNegation();
    Literal nq = q.getNegation();
    Literal nr = r.getNegation();
    Literal ns = s.getNegation();
    Clause cp = make(p);
    Clause cq = make(q);
    Clause cr = make(r);
    Clause cs = make(s);
    Clause cnp = make(np);
    Clause cnq = make(nq);
    Clause cpq = make(p, q);
    Clause cpqr = make(p, q, r);
    Clause cpnq = make(p, nq);

    // make sure assertions are turned on!
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testConstructor() {
        Formula f1 = new Formula(cp);
        assertEquals(1, f1.getSize());
        Formula f2 = new Formula(new Variable("a"));
        assertEquals(1, f2.getSize());
        Formula f3 = new Formula();
        assertEquals(0, f3.getSize());
    }

    @Test
    public void testGetClauses() {
        Formula f1 = new Formula(cp);
        f1 = f1.addClause(cq);
        ImList<Clause> clauses = new NonEmptyImList<Clause>(cp);
        clauses = clauses.add(cq);
        assertEquals(clauses, f1.getClauses());
    }

    @Test
    public void testAddClause() {
        Formula f1 = new Formula(cp);
        f1 = f1.addClause(cq);
        String s = f1.toString();
        assertEquals(2, f1.getSize());
        assertTrue(s.contains("Clause[Q]") && s.contains("Clause[P]"));
    }

    @Test
    public void testAnd() {
        Formula f1 = new Formula(cp);
        f1 = f1.addClause(cq);
        Formula f2 = new Formula(cr);
        f2 = f2.addClause(cs);
        f1 = f1.and(f2);
        String s = f1.toString();
        assertEquals(4, f1.getSize());
        assertTrue(s.contains("Clause[S]") && s.contains("Clause[Q]"));
    }

    @Test
    public void testOr() {
        Formula f1 = new Formula(cp);
        f1 = f1.addClause(cq);
        Formula f2 = new Formula(cr);
        f2 = f2.addClause(cs);
        f1 = f1.or(f2);
        String s = f1.toString();
        assertEquals(4, f1.getSize());
        assertTrue(s.contains("Clause[R, P]") || s.contains("Clause[P, R]"));
        assertTrue(s.contains("Clause[R, Q]") || s.contains("Clause[Q, R]"));
    }

    @Test
    public void testOrEmpty() {
        Formula f1 = new Formula();
        Formula f2 = new Formula(cp);
        f1 = f1.or(f2);
        assertEquals(1, f1.getSize());
    }

    @Test
    public void testNot() {
        Formula f1 = new Formula(cp.merge(cq));
        f1 = f1.addClause(cr);
        f1 = f1.not();
        String s = f1.toString();
        assertEquals(f1.getSize(), 2);
        assertTrue(s.contains("Clause[~Q, ~R]") || s.contains("Clause[~R, ~Q]"));
        assertTrue(s.contains("Clause[~P, ~R]") || s.contains("Clause[~R, ~P]"));
    }

    // Helper function for constructing a clause. Takes
    // a variable number of arguments, e.g.
    // clause(a, b, c) will make the clause (a or b or c)
    // @param e,... literals in the clause
    // @return clause containing e,...
    private Clause make(Literal... e) {
        Clause c = new Clause();
        for (int i = 0; i < e.length; ++i) {
            c = c.add(e[i]);
        }
        return c;
    }
}