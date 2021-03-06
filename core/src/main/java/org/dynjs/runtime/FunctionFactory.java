/**
 *  Copyright 2011 Douglas Campos
 *  Copyright 2011 dynjs contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime;

import org.dynjs.api.Function;
import org.dynjs.api.Scope;

import java.util.Deque;

public class FunctionFactory implements Function {

    private final Class<Function> clazz;

    public FunctionFactory(Class<Function> clazz) {
        this.clazz = clazz;
    }

    public static FunctionFactory create(Class<Function> clazz) {
        return new FunctionFactory(clazz);
    }

    @Override
    public Object call(DynThreadContext context, Scope scope, Object[] arguments) {
        Function function = instantiate();
        RT.paramPopulator((DynFunction) function, arguments);
        Deque<Function> callStack = context.getCallStack();
        callStack.push(function);
        Object result = function.call(context, scope, arguments);
        callStack.pop();
        return result;
    }

    private Function instantiate() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public Scope getEnclosingScope() {
        return null;
    }

    @Override
    public Object resolve(String name) {
        return null;
    }

    @Override
    public void define(String property, Object value) {
    }

}
