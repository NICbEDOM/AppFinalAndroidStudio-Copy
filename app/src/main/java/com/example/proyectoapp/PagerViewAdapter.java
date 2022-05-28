package com.example.proyectoapp;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerViewAdapter extends FragmentPagerAdapter {


    public PagerViewAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;
        switch (position)
        {
            case 0:
                fragment= new CursoInfo();
                break;

            case 1:
                fragment= new EstudiantesCursos_Frag();
                break;

            case 2:
                fragment= new novedades_frag();
                break;

            case 3:
                if(GlobalInfo.getUserActual().getRol().equals("Estudiante"))
                fragment= new ScanEvento();
                else
                fragment = new eventosDocente_frag();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
