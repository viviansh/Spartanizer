package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Common strategy of all evaluators$EvaluateExpression
 * @author Yossi Gil
 * @year 2016 */
abstract class $EvaluateInfixExpression extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  public static int indexForLeftEvaluation(final InfixExpression x) {
    final List<Expression> lst = extract.allOperands(x);
    int $ = 0;
    for (final Expression ¢ : lst) {
      if (!iz.number(¢))
        return $ > 1 ? $ : 0;
      ++$;
    }
    return 0;
  }

  public static int indexForRightEvaluation(final InfixExpression x) {
    final List<Expression> lst = extract.allOperands(x);
    for (int $ = 0, ¢ = lst.size() - 1; ¢ >= 0; --¢, ++$)
      if (!iz.number(lst.get(¢)))
        return $ > 1 ? $ : 0;
    return -1;
  }

  @Override public final String description() {
    return "Evaluate " + operation();
  }

  @Override public final String description(final InfixExpression ¢) {
    return description() + ":" + ¢;
  }

  @Override public final boolean prerequisite(final InfixExpression ¢) {
    return ¢.getOperator() == operator();
  }

  @Override public final ASTNode replacement(final InfixExpression x) {
    try {
      if (iz.validForEvaluation(x)) {
        final String $ = opportunisticReplacement(x);
        if ($ != null && $.length() < (x + "").length())
          return x.getAST().newNumberLiteral($);
      }
      if (indexForLeftEvaluation(x) > 1) {
        final int index = indexForLeftEvaluation(x);
        final InfixExpression cuttedExpression = subject.operands(extract.allOperands(x).subList(0, index)).to(operator());
        final List<Expression> afterExpressionOperands = extract.allOperands(x).subList(index, extract.allOperands(x).size());
        if (iz.validForEvaluation(cuttedExpression)) {
          final String str = opportunisticReplacement(cuttedExpression);
          if (str != null)
            return subject
                .pair(az.expression(x.getAST().newNumberLiteral(str)),
                    afterExpressionOperands.size() == 1 ? afterExpressionOperands.get(0) : subject.operands(afterExpressionOperands).to(operator()))
                .to(operator());
        }
      }
      if (indexForRightEvaluation(x) > 1 && operator() != DIVIDE && operator() != REMAINDER) {
        final int index = indexForRightEvaluation(x);
        final InfixExpression cuttedExpression = subject
            .operands(extract.allOperands(x).subList(extract.allOperands(x).size() - index, extract.allOperands(x).size())).to(operator());
        final List<Expression> beforeExpressionOperands = extract.allOperands(x).subList(0, extract.allOperands(x).size() - index);
        if (iz.validForEvaluation(cuttedExpression)) {
          final String str = opportunisticReplacement(cuttedExpression);
          if (str != null)
            return subject.pair(
                beforeExpressionOperands.size() == 1 ? beforeExpressionOperands.get(0) : subject.operands(beforeExpressionOperands).to(operator()),
                az.expression(x.getAST().newNumberLiteral(str))).to(operator());
        }
      }
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      // This is not a bug: exception must be ignored; it tells us, e.g.,
      // that we cannot divide by zero.
      // Uncomment next code line to debug; comment it out in production mode.
      // /* Logging Java code */ monitor.logEvaluationError(this,e);
      return null;
    }
    return null;
  }

  @Override public final TipperGroup tipperGroup() {
    return super.tipperGroup();
  }

  abstract double evaluateDouble(List<Expression> xs) throws IllegalArgumentException;

  abstract int evaluateInt(List<Expression> xs) throws IllegalArgumentException;

  abstract long evaluateLong(List<Expression> xs) throws IllegalArgumentException;

  abstract String operation();

  abstract Operator operator();

  private String opportunisticReplacement(final InfixExpression ¢) throws IllegalArgumentException {
    return type.of(¢) == INT ? Integer.toString(evaluateInt(extract.allOperands(¢)))
        : type.of(¢) == DOUBLE ? Double.toString(evaluateDouble(extract.allOperands(¢)))
            : type.of(¢) == LONG ? Long.toString(evaluateLong(extract.allOperands(¢))) + "L" : null;
  }
}