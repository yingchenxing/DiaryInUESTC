package edu.uestc.diaryinuestc.ui.todo.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.uestc.diaryinuestc.ui.todo.Todo;

public class TodoEngine {
    final TodoDao todoDao;

    public TodoEngine(Context context) {
        TodoDatabase todoDatabase = TodoDatabase.getInstance(context);
        todoDao = todoDatabase.getTodoDao();
    }

    //插入
    public void insertTodo(Todo... todos) {
        new InsertAsyncTack(todoDao).execute(todos);
    }

    static class InsertAsyncTack extends AsyncTask<Todo, Void, Void> {
        final TodoDao dao;

        public InsertAsyncTack(TodoDao todoDao) {
            dao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            dao.insertTodo(todos);
            return null;
        }
    }

    //更新
    public void updateTodos(Todo... todos) {
        new UpdateAsyncTack(todoDao).execute(todos);
    }

    static class UpdateAsyncTack extends AsyncTask<Todo, Void, Void> {
        final TodoDao dao;

        public UpdateAsyncTack(TodoDao todoDao) {
            dao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            dao.updateTodos(todos);
            return null;
        }
    }

    //删除
    public void deleteTodos(Todo... todos) {
        new DeleteAsyncTack(todoDao).execute(todos);
    }

    static class DeleteAsyncTack extends AsyncTask<Todo, Void, Void> {
        private TodoDao dao;

        public DeleteAsyncTack(TodoDao todoDao) {
            dao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            dao.deleteTodos(todos);
            return null;
        }
    }


    //全部删除
    public void deleteAll() {
        new DeleteAllAsyncTack(todoDao).execute();
    }

    static class DeleteAllAsyncTack extends AsyncTask<Void, Void, Void> {
        private TodoDao dao;

        public DeleteAllAsyncTack(TodoDao todoDao) {
            dao = todoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    //查询
    public List<Todo> queryAllStudent() {

        List<Todo> todoList = null;
        try {
            todoList = new QueryAsyncTack(todoDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  todoList;
    }

    static class QueryAsyncTack extends AsyncTask<Void, Void, List<Todo>> {
        private TodoDao dao;

        public QueryAsyncTack(TodoDao todoDao) {
            dao = todoDao;
        }

        @Override
        protected List<Todo> doInBackground(Void... voids) {
            List<Todo> todoList = dao.getAll();
            for (Todo todo : todoList) {
                Log.i("TODO", todo.toString());
            }
            return todoList;
        }
    }
}
