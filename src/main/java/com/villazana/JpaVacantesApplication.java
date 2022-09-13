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

    @Override
    public void run(String... args) throws Exception {
        buscarTodosOrdenados();
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

    /*Query Methods
     * */

    /* Query Methods Buscar vacantes por varios estatus (in)........................
     * */
    private void buscarVacantesVariosEstatus() { // método creado
        String[] estatus = new String[]{"Eliminada", "Creada"}; // arreglo con varios estatus
        List<Vacante> lista = repoVacante.findByEstatusIn(estatus);
        System.out.println("Registros encontrados " + lista.size());
        for (Vacante v : lista) {
            System.out.println(v.getId() + ": " + v.getNombre() + ": $" + v.getSalario() + " " + v.getEstatus());

        }
    }

    /* Query Methods Buscar vacantes por rando de salario y ordenados des.......................
     * */
    private void buscarVacantesSalario() { // método creado
        List<Vacante> lista = repoVacante.findBySalarioBetweenOrderBySalarioDesc(7000, 14000);
        System.out.println("Registros encontrados " + lista.size());
        for (Vacante v : lista) {
            System.out.println(v.getId() + ": " + v.getNombre() + ": $" + v.getSalario());

        }
    }

    /* Query Methods Buscar vacantes por destacados y estatus ordenados por id descendente........................
     * */
    private void buscarVacantesPorDestacadoEstatus() { // método creado
        List<Vacante> lista = repoVacante.findByDestacadoAndEstatusOrderByIdDesc(1, "Aprobada");
        System.out.println("Registros encontrados " + lista.size());
        for (Vacante v : lista) {
            System.out.println(v.getId() + ": " + v.getNombre() + ": " + v.getEstatus() + ": " + v.getDestacado());
        }
    }

    /* Query Methods Buscar vacantes por estatus.........................
     * */
    private void buscarVacantesPorEstatus() { // método creado
        List<Vacante> lista = repoVacante.findByEstatus("Aprobada");
        for (Vacante v : lista) {
            System.out.println(v.getId() + ": " + v.getNombre() + ": " + v.getEstatus());
            System.out.println("Registros encontrados " + lista.size());
        }
    }

    private void guardarVacante() { // para guardar una nueva vac necesito la cat
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

    private void buscarVacantes() { // como usas la cat debes usar el @OneToOne
        List<Vacante> lista = repoVacante.findAll();
        for (Vacante v : lista) {
            System.out.println(v.getId() + " " + v.getNombre() + " " + v.getCategoria().getNombre());
        }
    }

    /*..................... Métodos de JPARepository ......................... */

    /* Método findAll(con paginación y ordenado) - JpaRepository (Interfaz PagingAndSorting)
     * */
    private void buscarTodosPaginacionOrdenada() {
        Page<Categoria> page = repoCategorias.findAll(PageRequest.of(2, 5, Sort.by("nombre").descending()));
        System.out.println("total registros = " + page.getTotalElements());
        System.out.println("total paginas = " + page.getTotalPages());
        for (Categoria c : page.getContent()) {
            System.out.println(c.getId() + " " + c.getNombre());
        }
    }

    /* Método findAll(con paginación) - JpaRepository (Interfaz PagingAndSorting)
     * */
    private void buscarTodosPaginacion() {
        Page<Categoria> page = repoCategorias.findAll(PageRequest.of(1, 5));
        System.out.println("total registros = " + page.getTotalElements());
        System.out.println("total paginas = " + page.getTotalPages());
        for (Categoria c : page.getContent()) {
            System.out.println(c.getId() + " " + c.getNombre());
        }
    }

    /* Método findAll(ordenadas por un campo) - JpaRepository (Interfaz PagingAndSorting)
     * */
    private void buscarTodosOrdenados() {
        List<Categoria> categorias = repoCategorias.findAll(Sort.by("nombre").descending()); // igual al tributo de la clase
        for (Categoria c : categorias) {
            System.out.println(c.getId() + " " + c.getNombre());
        }
    }

    /* Método findAll- Interfaz JpaRepository - a través de la lista
     * */
    private void buscarTodasJpa() {
        List<Categoria> categorias = repoCategorias.findAll();
        for (Categoria c : categorias) {
            System.out.println(c.getId() + " " + c.getNombre());
        }
    }

    /* Método deleteAllInBatch  - Interfaz JpaRepository - elimina todo
     * */
    private void borrarTodoEnBloque() {
        repoCategorias.deleteAllInBatch();
    }

    /* ................Métodos de CrudRepository ......................... */


    /* Método saveAll- Interfaz CrudRepository
     * */
    private void guardarTodas() { // guarda varias entidades
        List<Categoria> categorias = getListaCategorias();
        repoCategorias.saveAll(categorias);
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

    /* Método findAllById - Interfaz CrudRepository
     * */
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

    /* Método  existsById- Interfaz CrudRepository-regresa verdadero o falso si existe  o no un registro
     * */
    private void existeId() {
        boolean existe = repoCategorias.existsById(5);
        System.out.println("exite la categoria? = " + existe);
    }

    /* Método count - Interfaz CrudRepository
     * */
    private void conteo() {
        long count = repoCategorias.count();
        System.out.println("N° de categorias= " + count);
    }

    /* Método deleteAll - Interfaz CrudRepository- elimina el archivo total
     * */
    private void eliminarTodos() {
        repoCategorias.deleteAll();

    }

    /*..........................................................................*/

    /* Método  findAll- Interfaz CrudRepository
     * */
    private void buscarTodos() {
        Iterable<Categoria> categorias = repoCategorias.findAll();
        for (Categoria cat : categorias) {
            System.out.println(cat);
        }
    }

    /* Método deleteById - Interfaz CrudRepository
     * */
    private void eliminar() {
        int idCategoria = 20;
        repoCategorias.deleteById(idCategoria);
    }

    /* Método save(update) - Interfaz CrudRepository
     * */
    private void modificar() {
        Optional<Categoria> optional = repoCategorias.findById(20);
        if (optional.isPresent()) {
            Categoria catTmp = optional.get();
            catTmp.setNombre("Manicurista y peinadora");
            catTmp.setDescripcion("Arte de uñas");
            repoCategorias.save(catTmp);
            System.out.println(optional.get());
        } else
            System.out.println("categoria no encontrada");
    }

    /* Método findById - Interfaz CrudRepository
     * */
    private void buscarPorId() {
        Optional<Categoria> optional = repoCategorias.findById(4);
        if (optional.isPresent())
            System.out.println(optional.get());
        else
            System.out.println("categoria no encontrada");
    }

    /* Método save - Interfaz CrudRepository
     * */
    private void guardar() {
        Categoria cat = new Categoria(); // creamos un objeto
        cat.setNombre("Finanzas");
        cat.setDescripcion("Trabajos relacionados con finanzas y contabilidad");
        repoCategorias.save(cat);
        System.out.println(cat);
    }

}
