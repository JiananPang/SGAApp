package rose.pangj.sga_app

import com.bignerdranch.expandablerecyclerview.Model.ParentObject


class SenateMessage(var message: Message) : ParentObject {

    /* Create an instance variable for your list of children */
    var mChildrenList = ArrayList<Message>()
    /**
     * Your constructor and any other accessor
     * methods should go here.
     */

    override fun getChildObjectList(): List<Any>? {
        return mChildrenList
    }

    override fun setChildObjectList(list: List<Any>) {
        mChildrenList = ArrayList()
    }
}