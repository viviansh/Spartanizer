package org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.spartan.refactoring.spartanizations.Spartanization;

/**
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 *
 * @author Yossi Gil
 * @since 2015-07-16
 */
public enum All {
  ;
  /**
   * Obtains a list of operands.
   *
   * @param e JD
   * @return a list of all operands of the parameter, ordered from left to right
   */
  public static List<Expression> operands(final InfixExpression e) {
    if (e == null)
      return null;
    final List<Expression> $ = new ArrayList<>();
    $.add(e.getLeftOperand());
    $.add(e.getRightOperand());
    if (e.hasExtendedOperands())
      $.addAll(e.extendedOperands());
    return $;
  }
  public static List<ICompilationUnit> compilationUnits() {
    try {
      return Spartanization.getAllProjectCompilationUnits(Funcs.getCompilationUnit(), new NullProgressMonitor());
    } catch (final JavaModelException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}
