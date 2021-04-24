/*
 * Licensed to Machnos under one or more contributor license
 * agreements. Machnos licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.machnos.api.gateway.server.domain.api.functions;

import com.machnos.api.gateway.server.domain.api.variables.NumberVariable;
import com.machnos.api.gateway.server.domain.api.variables.Variables;

import java.util.HashMap;
import java.util.Map;

/**
 * Function that can execute mathematical calculations on <code>NumberVariables</code> instances.
 */
public class MathFunction extends AbstractFunction {

    /**
     * The name of the function.
     */
    private static final String NAME = "Math";

    /**
     * The input key under which the variable is named that is the source of the calculation.
     */
    public static final String INPUT_KEY_SOURCE_VARIABLE_NAME = "source";

    /**
     * The input key under which the function that should be executed is stored.
     */
    public static final String INPUT_KEY_FUNCTION = "function";

    /**
     * The input key under which the variable is named that is the target of the calculation.
     */
    public static final String INPUT_KEY_TARGET_VARIABLE_NAME = "target";

    /**
     * The input key under which the variable is named that is the first parameter of the calculation.
     */
    public static final String INPUT_KEY_FUNCTION_PARAM1_VARIABLE_NAME = "function_param_1";

    /**
     * Function value representing an addition.
     *
     * @see #INPUT_KEY_FUNCTION
     */
    public static final String FUNCTION_ADD = "add";

    /**
     * Function value representing a subtraction.
     *
     * @see #INPUT_KEY_FUNCTION
     */
    public static final String FUNCTION_SUBTRACT = "subtract";

    /**
     * Function value representing a multiplicity.
     *
     * @see #INPUT_KEY_FUNCTION
     */
    public static final String FUNCTION_MULTIPLY = "multiply";

    /**
     * Function value representing a division.
     *
     * @see #INPUT_KEY_FUNCTION
     */
    public static final String FUNCTION_DIVIDE = "divide";

    @Override
    public String getId() {
        return MACHNOS_FUNCTION_ID_PREFIX + NAME;
    }

    @Override
    public String getName() {
        return MACHNOS_FUNCTION_NAME_PREFIX + NAME;
    }

    /**
     * Execute a mathematical function.
     *
     * The <code>functionConfiguration</code> map should at least contain three items:
     * <ol>
     *     <li>The source of the calculation under the key {@link #INPUT_KEY_SOURCE_VARIABLE_NAME}</li>
     *     <li>The function to execute under the key {@link #INPUT_KEY_TARGET_VARIABLE_NAME}</li>
     *     <li>The parameter for the execution of the function under the key {@link #INPUT_KEY_FUNCTION_PARAM1_VARIABLE_NAME}</li>
     * </ol>
     * The source variable will contain the result of the calculation unless you specify the {@link #INPUT_KEY_TARGET_VARIABLE_NAME} configuration item.
     * <p/>
     * So if you, for example, want to add two variable to each other you can configure the function as follow:
     * <pre>
     *     {@code
     *     var var1 = new NumberVariable().setName("var1").setValue("1");
     *     var var2 = new NumberVariable().setName("var2").setValue("2");
     *
     *     var variables = new Variables().add(var1).add(var2);
     *     var configuration = new HashMap<String, String>();
     *     // Configure the source of the addition.
     *     configuration.put(INPUT_KEY_SOURCE_VARIABLE_NAME, var1.getName());
     *     // Configure the function.
     *     configuration.put(INPUT_KEY_FUNCTION, MathFunction.FUNCTION_ADD);
     *     // Configure the first parameter of the function.
     *     configuration.put(INPUT_KEY_FUNCTION_PARAM1_VARIABLE_NAME, var2.getName());
     *     // Configure the name of the variable that should contain the result of the addition.
     *     configuration.put(INPUT_KEY_FUNCTION_INPUT_KEY_TARGET_VARIABLE_NAME, "var3");
     *
     *     var result = new MathFunction().execute(variables, configuration);
     *     if (result.hasExceptions()) {
     *       // Error
     *     } else {
     *         var resultOfCalculation = result.getOutputVariables().getNumberVariable("var3");
     *     }
     * </pre>
     *
     * @param inputVariables The input <code>Variables</code>.
     * @param functionConfiguration The configuration settings of the <code>Function</code>.
     * @return The <code>Result</code> of the execution.
     */
    @Override
    public Result execute(Variables inputVariables, Map<String, String> functionConfiguration) {
        final var result = new Result();
        final String sourceKey = functionConfiguration.get(INPUT_KEY_SOURCE_VARIABLE_NAME);
        final String targetKey = functionConfiguration.get(INPUT_KEY_TARGET_VARIABLE_NAME);
        final String function = functionConfiguration.get(INPUT_KEY_FUNCTION);
        final String param1Key = functionConfiguration.get(INPUT_KEY_FUNCTION_PARAM1_VARIABLE_NAME);

        final var source = inputVariables.getNumberVariable(sourceKey);
        final var param1 = inputVariables.getNumberVariable(param1Key);
        var target = inputVariables.getNumberVariable(targetKey);

        // Input validation
        requireVariable(sourceKey, source, result);
        requireVariableWithValue(param1Key, param1, result);
        if (result.hasExceptions()) {
            return result;
        }
        if (target == null) {
            target = new NumberVariable().setName(targetKey);
        }
        target
                .setScale(source.getScale())
                .setPrecision(source.getPrecision())
                .setRoundingMode(source.getRoundingMode())
                .setValue(source.getValue());


        if ("add".equals(function)) {
            target.add(param1);
        } else if ("subtract".equals(function)) {
            target.subtract(param1);
        } else if ("multiply".equals(function)) {
            target.multiply(param1);
        } else if ("divide".equals(function)) {
            target.divide(param1);
        }
        return result.addOutputVariable(target);
    }

    /**
     * Add a <code>NumberVariable</code> to another <code>NumberVariable</code>.
     *
     * @param source The source <code>NumberVariable</code> of the addition.
     * @param by The <code>NumberVariable</code> to add to the source.
     * @param into The name of the target <code>NumberVariable</code>. When set to <code>null</code> the source <code>NumberVariable</code> will contain the result of the addition.
     * @return The <code>Result</code> of the addition.
     */
    public static Result add(NumberVariable source, NumberVariable by, String into) {
        return prepareAndExecuteFunction(source, by, FUNCTION_ADD, into);
    }

    /**
     * Subtract a <code>NumberVariable</code> from another <code>NumberVariable</code>.
     *
     * @param source The source <code>NumberVariable</code> of the subtraction.
     * @param by The <code>NumberVariable</code> to subtract from the source.
     * @param into The name of the target <code>NumberVariable</code>. When set to <code>null</code> the source <code>NumberVariable</code> will contain the result of the subtraction.
     * @return The <code>Result</code> of the subtraction.
     */
    public static Result subtract(NumberVariable source, NumberVariable by, String into) {
        return prepareAndExecuteFunction(source, by, FUNCTION_SUBTRACT, into);
    }

    /**
     * Multiply a <code>NumberVariable</code> by another <code>NumberVariable</code>.
     *
     * @param source The source <code>NumberVariable</code> of the multiplicity.
     * @param by The <code>NumberVariable</code> to multiply the source by.
     * @param into The name of the target <code>NumberVariable</code>. When set to <code>null</code> the source <code>NumberVariable</code> will contain the result of the multiplicity.
     * @return The <code>Result</code> of the multiplicity.
     */
    public static Result multiply(NumberVariable source, NumberVariable by, String into) {
        return prepareAndExecuteFunction(source, by, FUNCTION_MULTIPLY, into);
    }

    /**
     * Divide a <code>NumberVariable</code> by another <code>NumberVariable</code>.
     *
     * @param source The source <code>NumberVariable</code> of the division.
     * @param by The <code>NumberVariable</code> to divide the source by.
     * @param into The name of the target <code>NumberVariable</code>. When set to <code>null</code> the source <code>NumberVariable</code> will contain the result of the division.
     * @return The <code>Result</code> of the division.
     */
    public static Result divide(NumberVariable source, NumberVariable by, String into) {
        return prepareAndExecuteFunction(source, by, FUNCTION_DIVIDE, into);
    }

    /**
     * Helper method to prepare a new <code>MathFunction</code> instance and execute it immediately.
     *
     * @param source The source of the calculation.
     * @param param1 The first parameter of the calculation.
     * @param function The math function to execute.
     * @param into The name of the result variable.
     * @return The <code>Result</code> of the execution.
     */
    private static Result prepareAndExecuteFunction(NumberVariable source, NumberVariable param1, String function, String into) {
        final var variables = new Variables().add(source).add(param1);
        final var keyMap = new HashMap<String, String>();
        keyMap.put(INPUT_KEY_SOURCE_VARIABLE_NAME, source.getName());
        keyMap.put(INPUT_KEY_TARGET_VARIABLE_NAME, into == null ? source.getName() : into);
        keyMap.put(INPUT_KEY_FUNCTION_PARAM1_VARIABLE_NAME, param1.getName());
        keyMap.put(INPUT_KEY_FUNCTION, function);
        return new MathFunction().execute(variables, keyMap);
    }
}
