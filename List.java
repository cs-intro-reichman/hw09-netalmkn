public class List {

    private Node first;
    private int size;

    public List() {
        first = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public CharData getFirst() {
        if (first == null) return null;
        return first.cp;
    }

    public void addFirst(char chr) {
        CharData cd = new CharData(chr);
        Node n = new Node(cd, first);
        first = n;
        size++;
    }

    // FIX: רווחים בין איברים + סוגריים חיצוניים
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            sb.append(current.cp.toString());
            if (current.next != null) sb.append(" ");
            current = current.next;
        }
        return "(" + sb.toString() + ")";
    }

    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.cp.chr == chr) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    public void update(char chr) {
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                current.cp.count++;
                return;
            }
            current = current.next;
        }
        addFirst(chr);
    }

    public boolean remove(char chr) {
        if (first == null) return false;

        if (first.cp.chr == chr) {
            first = first.next;
            size--;
            return true;
        }

        Node current = first;
        while (current.next != null) {
            if (current.next.cp.chr == chr) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public CharData get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        Node current = first;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        return current.cp;
    }

    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++] = current.cp;
            current = current.next;
        }
        return arr;
    }

    public ListIterator listIterator(int index) {
        if (size == 0) return null;

        Node current = first;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        return new ListIterator(current);
    }
}
