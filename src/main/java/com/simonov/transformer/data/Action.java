package com.simonov.transformer.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action
{
    private String name;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\"action\":\"");
        sb.append(name).append("\" }");
        return sb.toString();
    }
}

