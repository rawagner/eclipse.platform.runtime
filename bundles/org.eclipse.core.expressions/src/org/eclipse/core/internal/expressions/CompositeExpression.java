/*******************************************************************************
 * Copyright (c) 2003 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.core.internal.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;

public abstract class CompositeExpression extends Expression {
	
	private static final Expression[] EMPTY_ARRAY= new Expression[0]; 
	
	protected List fExpressions;
	
	public CompositeExpression() {
	}

	public void add(Expression expression) {
		if (fExpressions == null)
			fExpressions= new ArrayList(2);
		fExpressions.add(expression);
	}
	
	public Expression[] getChildren() {
		if (fExpressions == null)
			return EMPTY_ARRAY;
		return (Expression[])fExpressions.toArray(new Expression[fExpressions.size()]);
	}
	
	protected EvaluationResult evaluateAnd(IEvaluationContext scope) throws CoreException {
		if (fExpressions == null)
			return EvaluationResult.TRUE;
		EvaluationResult result= EvaluationResult.TRUE;
		for (Iterator iter= fExpressions.iterator(); iter.hasNext();) {
			Expression expression= (Expression)iter.next();
			result= result.and(expression.evaluate(scope));
			// keep iterating even if we have a not loaded found. It can be
			// that we find a false which will result in a better result.
			if (result == EvaluationResult.FALSE)
				return result;
		}
		return result;
	}
	
	protected EvaluationResult evaluateOr(IEvaluationContext scope) throws CoreException {
		if (fExpressions == null)
			return EvaluationResult.TRUE;
		EvaluationResult result= EvaluationResult.FALSE;
		for (Iterator iter= fExpressions.iterator(); iter.hasNext();) {
			Expression expression= (Expression)iter.next();
			result= result.or(expression.evaluate(scope));
			if (result == EvaluationResult.TRUE)
				return result;
		}
		return result;
	}	
}