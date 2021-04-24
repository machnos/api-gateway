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

package com.machnos.api.gateway.server.domain.api.variables;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * <code>Variable</code> implementation that encapsulates a BigDecimal value.
 */
public class NumberVariable extends AbstractVariable<BigDecimal, NumberVariable>{

    /**
     * The <code>MathContext</code> that will be applied on mathematical functions.
     */
    private MathContext mathContext = MathContext.DECIMAL64;

    /**
     * The scale of the value.
     */
    private int scale = 2;

    /**
     * Gives the scale that is used on the encapsulated number.
     *
     * @return The scale of the encapsulated number.
     */
    public int getScale() {
        return this.scale;
    }

    /**
     * Sets the scale of the value.
     *
     * @param scale The scale to use on the value.
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable setScale(int scale) {
        this.scale = scale;
        if (hasValue()) {
            // Scale the current value
            setValue(getValue());
        }
        return this;
    }

    /**
     * Gives the precision of this <code>NumberVariable</code>.
     *
     * @return The precision.
     */
    public int getPrecision() {
        return this.mathContext.getPrecision();
    }

    /**
     * Set the precision of the value during calculations.
     *
     * @param precision The precision of the value.
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable setPrecision(int precision) {
        this.mathContext = new MathContext(precision, this.mathContext.getRoundingMode());
        return this;
    }

    /**
     * Gives the <code>RoundingMode</code> of this <code>NumberVariable</code>.
     *
     * @return The <code>RoundingMode</code>.
     */
    public RoundingMode getRoundingMode() {
        return this.mathContext.getRoundingMode();
    }

    /**
     * Sets the rounding mode of the value.
     *
     * @param roundingMode The rounding mode.
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable setRoundingMode(RoundingMode roundingMode) {
        this.mathContext = new MathContext(this.mathContext.getPrecision(), roundingMode);
        return this;
    }

    /**
     * Sets the value and applies the precision and rounding mode.
     *
     * @param value The value to set.
     * @return The current instance of the <code>NumberVariable</code>.
     */
    @Override
    public NumberVariable setValue(BigDecimal value) {
        if (value == null) {
            return super.setValue(null);
        } else {
            return super.setValue(value.setScale(this.scale, this.mathContext.getRoundingMode()));
        }
    }

    /**
     * Adds the value of another <code>NumberVariable</code> to this value.
     *
     * @param augend The number to add.
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable add(NumberVariable augend) {
        if (!augend.hasValue()) {
            return this;
        }
        if (!hasValue()) {
            setValue(BigDecimal.ZERO);
        }
        return setValue(getValue().add(augend.getValue(), this.mathContext));
    }

    /**
     * Subtracts the value of another <code>NumberVariable</code> to this value.
     *
     * @param subtrahend The number to subtract.
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable subtract(NumberVariable subtrahend) {
        if (!subtrahend.hasValue()) {
            return this;
        }
        if (!hasValue()) {
            setValue(BigDecimal.ZERO);
        }
        return setValue(getValue().subtract(subtrahend.getValue(), this.mathContext));
    }

    /**
     * Multiplies the value of another <code>NumberVariable</code> to this value.
     *
     * @param multiplicand The number to multiply this value by.
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable multiply(NumberVariable multiplicand) {
        if (!multiplicand.hasValue()) {
            return this;
        }
        if (!hasValue()) {
            setValue(BigDecimal.ZERO);
        }
        return setValue(getValue().multiply(multiplicand.getValue(), this.mathContext));
    }

    /**
     * Divides the value of another <code>NumberVariable</code> to this value.
     *
     * @param divisor The number to divide this value by.
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable divide(NumberVariable divisor) {
        if (!divisor.hasValue()) {
            return this;
        }
        if (!hasValue()) {
            setValue(BigDecimal.ZERO);
        }
        return setValue(getValue().divide(divisor.getValue(), this.mathContext));
    }

    /**
     * Calculates the absolute value of the current value.
     *
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable absolute() {
        if (!hasValue()) {
            setValue(BigDecimal.ZERO);
        }
        return setValue(getValue().abs(this.mathContext));
    }

    /**
     * Gives the maximum value of this <code>NumberValue</code> and the other <code>NumberValue</code>.
     *
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable maximum(NumberVariable other) {
        if (!hasValue()) {
            setValue(BigDecimal.ZERO);
        }
        return setValue(getValue().max(other.getValue()));
    }

    /**
     * Gives the minumum value of this <code>NumberValue</code> and the other <code>NumberValue</code>.
     *
     * @return The current instance of the <code>NumberVariable</code>.
     */
    public NumberVariable minimum(NumberVariable other) {
        if (!hasValue()) {
            setValue(BigDecimal.ZERO);
        }
        return setValue(getValue().min(other.getValue()));
    }
}
