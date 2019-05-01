package com.example.msc.mpis_compiler.containers;

import com.example.msc.mpis_compiler.utilities.Utility;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by msc on 01/05/2019.
 */

public class CompileState {
    public int pc = 0;
    public boolean isScannedForLabels = false;
    public Set<Utility.ERROR> errors = new HashSet<>();
}
