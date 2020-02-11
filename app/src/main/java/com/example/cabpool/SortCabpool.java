package com.example.cabpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortCabpool {

    static List cabpools = new ArrayList<Cabpools>();
    static List key = new ArrayList<String>();

    static public List<Cabpools> sortCabpool(List<Cabpools> cabpoolList, List<String> keyList)
    {
        cabpools=cabpoolList;
        key=keyList;
        sort(cabpools,0,cabpoolList.size()-1);
        return cabpools;
    }

    static public List<String> sortKey(List<Cabpools> cabpoolList, List<String> keyList)
    {
        cabpools=cabpoolList;
        key=keyList;
        sort(cabpools,0,cabpoolList.size()-1);
        return keyList;
    }

    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
    static int partition(List<Cabpools> cabpoolList, int low, int high)
    {
        Cabpools pivot = cabpoolList.get(high);
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than the pivot
            if (CompareTime.isValid(pivot.getDate(),
                    pivot.getTime(),
                    cabpoolList.get(j).getDate(),
                    cabpoolList.get(j).getTime()))
            {
                i++;

                // swap arr[i] and arr[j]
                Collections.swap(cabpoolList,i,j);
                Collections.swap(key,i,j);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        Collections.swap(cabpoolList,i+1,high);
        Collections.swap(key,i+1,high);


        return i+1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    static void sort(List<Cabpools> cabpoolList, int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(cabpoolList, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(cabpoolList, low, pi-1);
            sort(cabpoolList, pi+1, high);
        }
    }
}
