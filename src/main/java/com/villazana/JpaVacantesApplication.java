package com.villazana;

import com.villazana.model.Categoria;
import com.villazana.model.Perfil;
import com.villazana.model.Usuario;
import com.villazana.model.Vacante;
import com.villazana.repository.CategoriasRepository;
import com.villazana.repository.PerfilesRepository;
import com.villazana.repository.UsuariosRepository;
import com.villazana.repository.VacantesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

@SpringBootApplication
public class JpaVacantesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JpaVacantesApplication.class, args);

    }

    @Autowired
    private VacantesRepository repoVacante;
    @Autowired
    private CategoriasRepository repoCategorias;

    @Autowired
    private PerfilesRepository repoPerfil;

    @Autowired
    private UsuariosRepository repoUsuario;

    private void buscarVacantesVariosEstatus(){
        String[]estatus = new String[]{"Eliminada","Creada"};
        List<Vacante>lista=repoVacante.findByEstatusIn(estatus);
        System.out.println("Registros encontrados " + lista.size());
        for (Vacante v : lista){
            System.out.println(v.getId()+": "+v.getNombre()+": $"+v.getSalario()+" "+v.getEstatus());

        }
    }

    private void buscarVacantesSalario(){
        List<Vacante>lista=repoVacante.findBySalarioBetweenOrderBySalarioDesc(7000,14000);
        System.out.println("Registros encontrados " + lista.size());
        for (Vacante v : lista){
            System.out.println(v.getId()+": "+v.getNombre()+": $"+v.getSalario());

        }
    }

    private void buscarVacantesPorDestacadoEstatus(){
        List<Vacante>lista=repoVacante.findByDestacadoAndEstatusOrderByIdDesc(1,"Aprobada");
        System.out.println("Registros encontrados " + lista.size());
        for (Vacante v : lista){
            System.out.println(v.getId()+": "+v.getNombre()+": "+v.getEstatus()+": "+v.getDestacado());

        }

    }

    private void buscarVacantesPorEstatus(){
        List<Vacante>lista=repoVacante.findByEstatus("Aprobada");
        for (Vacante v : lista){
            System.out.println(v.getId()+": "+v.getNombre()+": "+v.getEstatus());
            System.out.println("Registros encontrados " + lista.size());
        }
    }

    public void buscarUsuario() {
        Optional<Usuario> optional = repoUsuario.findById(1);
        if (optional.isPresent()) {
            Usuario u = optional.get();
            System.out.println("usuario = " + u.getNombre());
            System.out.println(" Perfiles asignados ");
            for (Perfil p : u.getPerfiles()) {
                System.out.println(p.getPerfil());
            }
        } else {
            System.out.println(" Usuario no encontrado");
        }
    }

    private void crearUsuarioConDosPerfiles() {
        Usuario user = new Usuario();
        user.setNombre("Wendy Villazana");
        user.setEmail("wendy@gmail.com");
        user.setFechaRegistro(new Date());
        user.setUsername("wvillazana");
        user.setPassword("12345");
        user.setEstatus(1);

        Perfil per1 = new Perfil();
        per1.setId(2);

        Perfil per2 = new Perfil();
        per2.setId(3);

        user.agregar(per1);
        user.agregar(per2);

        repoUsuario.save(user);
    }

    private void crearPerfilAplicacion() {
        repoPerfil.saveAll(perfilesAplicacion());
    }

    private void guardarVacante() { // para guardar una nueva la vac necesito la cat
        Vacante vacante = new Vacante();
        vacante.setNombre("profe de matematicas");
        vacante.setDescripcion("escuela primaria");
        vacante.setFecha(new Date());
        vacante.setSalario(8.500);
        vacante.setEstatus("Aprobada");
        vacante.setDestacado(0);
        vacante.setImagen("escuela.png");
        vacante.setDetalles("requisitos de profesor");
        Categoria cat = new Categoria();
        cat.setId(15);
        vacante.setCategoria(cat);
        repoVacante.save(vacante);
    }

    private void buscarVacantes() {
        List<Vacante> lista = repoVacante.findAll();
        for (Vacante v : lista) {
            System.out.println(v.getId() + " " + v.getNombre() + " " + v.getCategoria().getNombre());
        }

    }

    @Override
    public void run(String... args) throws Exception {

     buscarVacantesVariosEstatus();
    }

    //findAll -JPA Repository (con paginación y ordenados)
    private void buscarTodosPaginacionOrdenada() {
        Page<Categoria> page = repoCategorias.findAll(PageRequest.of(2, 5, Sort.by("nombre").descending()));
        System.out.println("total registros = " + page.getTotalElements());
        System.out.println("total paginas = " + page.getTotalPages());
        for (Categoria c : page.getContent()) {
            System.out.println(c.getId() + " " + c.getNombre());
        }
    }

    //findAll -JPA Repository (con paginación)

    private void buscarTodosPaginacion() {
        Page<Categoria> page = repoCategorias.findAll(PageRequest.of(1, 5));
        System.out.println("total registros = " + page.getTotalElements());
        System.out.println("total paginas = " + page.getTotalPages());
        for (Categoria c : page.getContent()) {
            System.out.println(c.getId() + " " + c.getNombre());
        }
    }

    //findAll -JPA Repository (order by)

    private void buscarTodosOrdenados() {
        List<Categoria> categorias = repoCategorias.findAll(Sort.by("nombre").descending()); // igual al tributo d ela clase
        for (Categoria c : categorias) {
            System.out.println(c.getId() + " " + c.getNombre());
        }
    }

    //deleteAllInBatch -JPA Repository  elimina todo registro de tabla

    private void borrarTodoEnBloque() {
        repoCategorias.deleteAllInBatch();
    }


    //findAll -JPA Repository- a traves de lista

    private void buscarTodasJpa() {
        List<Categoria> categorias = repoCategorias.findAll();
        for (Categoria c : categorias) {
            System.out.println(c.getId() + " " + c.getNombre());
        }
    }

    //   Métodos de CrudRepository

    private void guardarTodas() { // guarda varias entidades
        List<Categoria> categorias = getListaCategorias();
        repoCategorias.saveAll(categorias);
    }

    private void existeId() {
        boolean existe = repoCategorias.existsById(5);
        System.out.println("exite la categoria? = " + existe);
    }

    private void buscarTodos() {
        Iterable<Categoria> categorias = repoCategorias.findAll();
        for (Categoria cat : categorias) {
            System.out.println(cat);
        }
    }

    private void encontrarPorIds() { // para varios ids
        List<Integer> ids = new LinkedList<Integer>();
        ids.add(1);
        ids.add(4);
        ids.add(9);
        Iterable<Categoria> categorias = repoCategorias.findAllById(ids);
        for (Categoria cat : categorias) {
            System.out.println(cat);
        }
    }

    private void eliminartodos() { // permite liminar todo los registros de la tabla
        repoCategorias.deleteAll(); // no regresa nada x eso no se guarda en una variable

    }

    private void conteo() {
        long count = repoCategorias.count();
        System.out.println("N° de categorias= " + count);
    }

    private void eliminar() {
        int idCategoria = 2;
        repoCategorias.deleteById(idCategoria);
    }

    private void modificar() {
        Optional<Categoria> optional = repoCategorias.findById(5);
        if (optional.isPresent()) {
            Categoria catTmp = optional.get();
            catTmp.setNombre("Manicurista");
            catTmp.setDescripcion("Arte de uñas");
            repoCategorias.save(catTmp);
            System.out.println(optional.get());
        } else
            System.out.println("categoria no encontrada");
    }


    private void buscarPorId() {
        Optional<Categoria> optional = repoCategorias.findById(4);
        if (optional.isPresent())
            System.out.println(optional.get());
        else
            System.out.println("categoria no encontrada");
    }

    private void guardar() {
        Categoria cat = new Categoria();
        cat.setNombre("Finanzas");
        cat.setDescripcion("Trabajos relacionados con finanzas y contabilidad");
        repoCategorias.save(cat);
        System.out.println(cat);
    }

    private List<Categoria> getListaCategorias() {
        List<Categoria> lista = new LinkedList<Categoria>();
        Categoria cat1 = new Categoria();
        cat1.setNombre("Programador");
        cat1.setDescripcion("Trabajos para desarrolladores");

        Categoria cat2 = new Categoria();
        cat2.setNombre("Soldador");
        cat2.setDescripcion("Trabajos para soldadores");


        Categoria cat3 = new Categoria();
        cat3.setNombre("Ingeniero Industrial");
        cat3.setDescripcion("Trabajos en almacen");

        lista.add(cat1);
        lista.add(cat2);
        lista.add(cat3);
        return lista;
    }

    private List<Perfil> perfilesAplicacion() {
        List<Perfil> lista = new LinkedList<Perfil>();
        Perfil per1 = new Perfil();
        per1.setPerfil("supervisor");

        Perfil per2 = new Perfil();
        per2.setPerfil("Administrador");

        Perfil per3 = new Perfil();
        per3.setPerfil("Usuario");

        lista.add(per1);
        lista.add(per2);
        lista.add(per3);
        return lista;

    }


    {
        int n1, n2, suma;

        Scanner teclado = new Scanner( System.in );

        System.out.print( "Introduzca primer número: " );
        n1 = teclado.nextInt();

        System.out.print( "Introduzca segundo número: " );
        n2 = teclado.nextInt();

        suma = n1 + n2;

        System.out.println( "La suma de " + n1 + " más " + n2 + " es " + suma + "." );
    }
}
