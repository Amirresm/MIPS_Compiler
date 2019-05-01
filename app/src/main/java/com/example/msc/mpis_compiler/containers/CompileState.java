package com.example.msc.mpis_compiler.containers;

import com.example.msc.mpis_compiler.utilities.Utility;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Amirreza on 25/04/2019.
 */

public class CompileState {
    public boolean isScannedForLabels = false;
    public Set<Utility.ERROR> errors = new HashSet<>();
}
